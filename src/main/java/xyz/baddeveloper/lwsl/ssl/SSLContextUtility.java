package xyz.baddeveloper.lwsl.ssl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

public final class SSLContextUtility {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSLContextUtility.class);

    private static final SSLContextUtility INSTANCE = new SSLContextUtility();

    private SSLContextUtility() {
    }

    public static SSLContextUtility getInstance() {
        return INSTANCE;
    }

    public SSLContext getSslContext(SSLContextConfig config) {
        try {
            final SSLContext sslContext = SSLContext.getInstance(config.getProtocol());
            final KeyManager[] keyManagers = getKeyManagers(config);
            final TrustManager[] trustManagers = getTrustManagers(config);
            if (keyManagers != null && trustManagers != null) {
                sslContext.init(keyManagers, trustManagers, null);
                return sslContext;
            } else {
                LOGGER.error("Failed to create an SSLContext using configuration: {}", config);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to create SSLContext using configuration: " + config, e);
        }
        return null;
    }

    private KeyManager[] getKeyManagers(SSLContextConfig config) {
        final KeyManagerFactory kmf = initKeyStore(config);
        KeyManager[] keyManagers = null;
        if (kmf != null) {
            keyManagers = getKeyManagers(kmf, config);
        }
        return keyManagers;
    }

    private KeyManager[] getKeyManagers(KeyManagerFactory kmf, SSLContextConfig config) {
        final String serverAlias = config.getKeystoreServerAlias();
        final String clientAlias = config.getKeystoreClientAlias();
        if (serverAlias != null || clientAlias != null) {
            return new KeyManager[]{new CustomKeyManager((X509ExtendedKeyManager) kmf.getKeyManagers()[0], serverAlias, clientAlias)};
        } else {
            return kmf.getKeyManagers();
        }
    }

    private TrustManager[] getTrustManagers(SSLContextConfig config) {
        final TrustManagerFactory tmf = initTrustStore(config);
        TrustManager[] trustManagers = null;
        if (tmf != null) {
            trustManagers = tmf.getTrustManagers();
        }
        return trustManagers;
    }

    private KeyStore getStoreType(String storeType, String provider) {
        try {
            if (provider == null) {
                LOGGER.warn("No provider was supplied for store type of '{}'.", storeType);
                return KeyStore.getInstance(storeType);
            } else {
                return KeyStore.getInstance(storeType, provider);
            }
        } catch (KeyStoreException e) {
            LOGGER.error("Failed to default store type of: " + storeType, e);
        } catch (NoSuchProviderException e) {
            LOGGER.error("Failed to find a provider of type: " + provider, e);
        }
        return null;
    }

    private KeyManagerFactory initKeyStore(SSLContextConfig config) {
        final KeyStore keyStore = getStoreType(config.getKeystoreType(), config.getKeystoreProvider());
        if (keyStore == null) {
            return null;
        }

        try {
            loadStore(keyStore, config.getKeystoreLocation(), config.getKeystorePassword());
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            LOGGER.error("Failed to load key store.", e);
            return null;
        }

        LOGGER.info("Printing keystore details.");
        printKeystore(keyStore);

        KeyManagerFactory kmf = loadKeyManagerFactory(config);
        if (kmf != null) {
            try {
                kmf.init(keyStore, config.getKeystorePassword().toCharArray());
                return kmf;
            } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                LOGGER.error("Failed to initialise key manager factory.", e);
            }
        }
        return null;
    }

    private void printKeystore(KeyStore store) {
        final Enumeration<String> aliases;
        try {
            aliases = store.aliases();
            while (aliases.hasMoreElements()) {
                final String nextElement = aliases.nextElement();
                final String endingNewline = aliases.hasMoreElements() ? "" : "\n";
                LOGGER.info("Found alias '{}'.{}", nextElement, endingNewline);
            }
        } catch (KeyStoreException e) {
            LOGGER.error("Failed to print aliases from store.", e);
        }
    }

    private KeyManagerFactory loadKeyManagerFactory(SSLContextConfig config) {
        try {
            return KeyManagerFactory.getInstance(config.getKeyManager());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Failed to load key manager factory: " + config.getKeyManager(), e);
        }
        return null;
    }

    private TrustManagerFactory initTrustStore(SSLContextConfig config) {
        // If no trust store property is set, we will try to load system default truststore
        if (!hasTrustStoreProperty(config)) {
            LOGGER.info("No truststore properties set. Loading the default JVM trust manager.");
            return loadDefaultJVMTrustManager();
        } else {
            final KeyStore trustStore = getStoreType(config.getTruststoreType(), config.getTruststoreProvider());
            if (trustStore == null) {
                return null;
            }

            try {
                loadStore(trustStore, config.getTruststoreLocation(), config.getTruststorePassword());
            } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
                LOGGER.error("Failed to load trust store.", e);
                return null;
            }

            LOGGER.info("Printing truststore details.");
            printKeystore(trustStore);

            TrustManagerFactory tmf = loadTrustManagerFactory(config);
            if (tmf != null) {
                try {
                    tmf.init(trustStore);
                    return tmf;
                } catch (KeyStoreException e) {
                    LOGGER.error("Failed to initialise trust manager factory.", e);
                }
            }
            return null;
        }
    }

    private boolean hasTrustStoreProperty(SSLContextConfig config) {
        return config.getTruststoreLocation() != null || config.getTruststorePassword() != null;
    }

    private TrustManagerFactory loadDefaultJVMTrustManager() {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);

            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    X509TrustManager x509TrustManager = (X509TrustManager) trustManager;
                    LOGGER.info("Printing truststore details.");
                    printX509TrustManager(x509TrustManager);
                }
            }
            return trustManagerFactory;
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            LOGGER.error("Failed to initialise trust manager factory.", e);
        }

        return null;
    }

    private void printX509TrustManager(X509TrustManager trustManager) {
        final X509Certificate[] certs = trustManager.getAcceptedIssuers();
        for (int i = 0; i < certs.length; i++) {
            final String certDN = certs[i].getSubjectDN().getName();
            final String endingNewline = (i < certs.length - 1) ? "" : "\n";
            LOGGER.info("Found trusted cert '{}'.{}", certDN, endingNewline);
        }
    }

    private TrustManagerFactory loadTrustManagerFactory(SSLContextConfig config) {
        try {
            return TrustManagerFactory.getInstance(config.getTrustManager());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Failed to load trust manager factory: " + config.getTrustManager(), e);
        }
        return null;
    }

    private void loadStore(KeyStore keyStore, String storeLocation, String password) throws IOException, NoSuchAlgorithmException, CertificateException {
        keyStore.load(storeLocation == null ? null : getStoreInputStream(storeLocation), password.toCharArray());
    }

    public InputStream getStoreInputStream(String storeLocation) throws IOException {
        // Look for the store file on the filesystem first, then on the classpath
        File filesystemFile = new File(storeLocation);
        if (filesystemFile.exists()) {
            return new FileInputStream(filesystemFile);
        }

        final InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(storeLocation);
        if (resourceAsStream == null) {
            throw new IOException("No file corresponding to '" + storeLocation + "' was found on the classpath or filesystem.");
        }
        return resourceAsStream;
    }

}
