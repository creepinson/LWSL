package xyz.baddeveloper.lwsl.ssl;

import xyz.baddeveloper.lwsl.exceptions.SSLException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedKeyManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public final class SSLContextUtility {

    private static final SSLContextUtility INSTANCE = new SSLContextUtility();

    private SSLContextUtility() {
    }

    public static SSLContextUtility getInstance() {
        return INSTANCE;
    }

    public SSLContext getSslContext(SSLContextConfig config) throws SSLException, KeyManagementException, NoSuchAlgorithmException {
        try {
            final SSLContext sslContext = SSLContext.getInstance(config.getProtocol());
            final KeyManager[] keyManagers = getKeyManagers(config);
            final TrustManager[] trustManagers = getTrustManagers(config);
            if (keyManagers != null && trustManagers != null) {
                sslContext.init(keyManagers, trustManagers, null);
                return sslContext;
            } else {
                throw new SSLException(String.format("Failed creating an SSLContext with %s", config.toString()));
            }
        } catch (SSLException | KeyManagementException | NoSuchAlgorithmException e) {
            throw e;
        }
    }

    private KeyManager[] getKeyManagers(SSLContextConfig config) throws SSLException {
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

    private TrustManager[] getTrustManagers(SSLContextConfig config) throws SSLException {
        final TrustManagerFactory tmf = initTrustStore(config);
        TrustManager[] trustManagers = null;
        if (tmf != null) {
            trustManagers = tmf.getTrustManagers();
        }
        return trustManagers;
    }

    private KeyStore getStoreType(String storeType, String provider) throws SSLException {
        try {
            if (provider == null) {
                return KeyStore.getInstance(storeType);
            } else {
                return KeyStore.getInstance(storeType, provider);
            }
        } catch (KeyStoreException | NoSuchProviderException e) {
            throw new SSLException(e.getMessage());
        }
    }

    private KeyManagerFactory initKeyStore(SSLContextConfig config) throws SSLException {
        final KeyStore keyStore = getStoreType(config.getKeystoreType(), config.getKeystoreProvider());

        try {
            loadStore(keyStore, config.getKeystoreLocation(), config.getKeystorePassword());
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new SSLException(String.format("Failed loading keystore (%s)", e.getMessage()));
        }

        KeyManagerFactory kmf = loadKeyManagerFactory(config);
        if (kmf != null) {
            try {
                kmf.init(keyStore, config.getKeystorePassword().toCharArray());
                return kmf;
            } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
                throw new SSLException(e.getMessage());
            }
        }
        return null;
    }

    private KeyManagerFactory loadKeyManagerFactory(SSLContextConfig config) throws SSLException {
        try {
            return KeyManagerFactory.getInstance(config.getKeyManager());
        } catch (NoSuchAlgorithmException e) {
            throw new SSLException(e.getMessage());
        }
    }

    private TrustManagerFactory initTrustStore(SSLContextConfig config) throws SSLException {
        if (!hasTrustStoreProperty(config)) {
            return loadDefaultJVMTrustManager();
        } else {
            final KeyStore trustStore = getStoreType(config.getTruststoreType(), config.getTruststoreProvider());

            try {
                loadStore(trustStore, config.getTruststoreLocation(), config.getTruststorePassword());
            } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
                throw new SSLException(e.getMessage());
            }

            TrustManagerFactory tmf = loadTrustManagerFactory(config);
            if (tmf != null) {
                try {
                    tmf.init(trustStore);
                    return tmf;
                } catch (KeyStoreException e) {
                    throw new SSLException(e.getMessage());
                }
            }
            return null;
        }
    }

    private boolean hasTrustStoreProperty(SSLContextConfig config) {
        return config.getTruststoreLocation() != null || config.getTruststorePassword() != null;
    }

    private TrustManagerFactory loadDefaultJVMTrustManager() throws SSLException {
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);

            return trustManagerFactory;
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            throw new SSLException(e.getMessage());
        }
    }

    private TrustManagerFactory loadTrustManagerFactory(SSLContextConfig config) throws SSLException {
        try {
            return TrustManagerFactory.getInstance(config.getTrustManager());
        } catch (NoSuchAlgorithmException e) {
            throw new SSLException(e.getMessage());
        }
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
