package xyz.baddeveloper.lwsl.ssl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;

public class SSLContextConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSLContextConfig.class);
    private static final String PASSWORD_MASK = "*****";

    private String protocol;
    private String keyManager;
    private String trustManager;
    private String keystoreType;
    private String keystoreProvider;
    private String keystoreLocation;
    private String keystorePassword;
    private String keystoreServerAlias;
    private String keystoreClientAlias;
    private String truststoreType;
    private String truststoreProvider;
    private String truststoreLocation;
    private String truststorePassword;

    private SSLContextConfig(Builder builder) {
        this.protocol = builder.protocol;
        this.keyManager = builder.keyManager;
        this.trustManager = builder.trustManager;
        this.keystoreType = builder.keystoreType;
        this.keystoreProvider = builder.keystoreProvider;
        this.keystoreLocation = builder.keystoreLocation;
        this.keystorePassword = builder.keystorePassword;
        this.keystoreServerAlias = builder.keystoreServerAlias;
        this.keystoreClientAlias = builder.keystoreClientAlias;
        this.truststoreType = builder.truststoreType;
        this.truststoreProvider = builder.truststoreProvider;
        this.truststoreLocation = builder.truststoreLocation;
        this.truststorePassword = builder.truststorePassword;
        validatePasswords();
    }

    private void validatePasswords() {
        if (this.keystorePassword == null) {
            LOGGER.warn("The keystore password was not set. You may not be able to open the keystore.");
        }

        if (this.truststorePassword == null) {
            LOGGER.warn("The truststore password was not set. You may not be able to open the truststore.");
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public String getKeyManager() {
        return keyManager;
    }

    public String getTrustManager() {
        return trustManager;
    }

    public String getKeystoreType() {
        return keystoreType;
    }

    public String getKeystoreProvider() {
        return keystoreProvider;
    }

    public String getKeystoreLocation() {
        return keystoreLocation;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getKeystoreServerAlias() {
        return keystoreServerAlias;
    }

    public String getKeystoreClientAlias() {
        return keystoreClientAlias;
    }

    public String getTruststoreType() {
        return truststoreType;
    }

    public String getTruststoreProvider() {
        return truststoreProvider;
    }

    public String getTruststoreLocation() {
        return truststoreLocation;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    @Override
    public String toString() {
        return "SSLContextConfig{" +
                "protocol='" + protocol + '\'' +
                ", keyManager='" + keyManager + '\'' +
                ", trustManager='" + trustManager + '\'' +
                ", keystoreType='" + keystoreType + '\'' +
                ", keystoreProvider='" + keystoreProvider + '\'' +
                ", keystoreLocation='" + keystoreLocation + '\'' +
                ", keystorePassword='" + (keystorePassword == null ? null : PASSWORD_MASK) + '\'' +
                ", keystoreServerAlias='" + keystoreServerAlias + '\'' +
                ", keystoreClientAlias='" + keystoreClientAlias + '\'' +
                ", truststoreType='" + truststoreType + '\'' +
                ", truststoreProvider='" + truststoreProvider + '\'' +
                ", truststoreLocation='" + truststoreLocation + '\'' +
                ", truststorePassword='" + (truststorePassword == null ? null : PASSWORD_MASK) + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(o.getClass().equals(this.getClass()))) {
            return false;
        }

        SSLContextConfig that = (SSLContextConfig) o;

        if (protocol != null ? !protocol.equals(that.protocol) : that.protocol != null) {
            return false;
        }
        if (keyManager != null ? !keyManager.equals(that.keyManager) : that.keyManager != null) {
            return false;
        }
        if (trustManager != null ? !trustManager.equals(that.trustManager) : that.trustManager != null) {
            return false;
        }
        if (keystoreType != null ? !keystoreType.equals(that.keystoreType) : that.keystoreType != null) {
            return false;
        }
        if (keystoreProvider != null ? !keystoreProvider.equals(that.keystoreProvider) : that.keystoreProvider != null) {
            return false;
        }
        if (keystoreLocation != null ? !keystoreLocation.equals(that.keystoreLocation) : that.keystoreLocation != null) {
            return false;
        }
        if (keystorePassword != null ? !keystorePassword.equals(that.keystorePassword) : that.keystorePassword != null) {
            return false;
        }
        if (keystoreServerAlias != null ? !keystoreServerAlias.equals(that.keystoreServerAlias) : that.keystoreServerAlias != null) {
            return false;
        }
        if (keystoreClientAlias != null ? !keystoreClientAlias.equals(that.keystoreClientAlias) : that.keystoreClientAlias != null) {
            return false;
        }
        if (truststoreType != null ? !truststoreType.equals(that.truststoreType) : that.truststoreType != null) {
            return false;
        }
        if (truststoreProvider != null ? !truststoreProvider.equals(that.truststoreProvider) : that.truststoreProvider != null) {
            return false;
        }
        if (truststoreLocation != null ? !truststoreLocation.equals(that.truststoreLocation) : that.truststoreLocation != null) {
            return false;
        }
        return truststorePassword != null ? !truststorePassword.equals(that.truststorePassword) : that.truststorePassword != null;
    }

    @Override
    public int hashCode() {
        int result = protocol != null ? protocol.hashCode() : 0;
        result = 31 * result + (keyManager != null ? keyManager.hashCode() : 0);
        result = 31 * result + (trustManager != null ? trustManager.hashCode() : 0);
        result = 31 * result + (keystoreType != null ? keystoreType.hashCode() : 0);
        result = 31 * result + (keystoreProvider != null ? keystoreProvider.hashCode() : 0);
        result = 31 * result + (keystoreLocation != null ? keystoreLocation.hashCode() : 0);
        result = 31 * result + (keystorePassword != null ? keystorePassword.hashCode() : 0);
        result = 31 * result + (keystoreServerAlias != null ? keystoreServerAlias.hashCode() : 0);
        result = 31 * result + (keystoreClientAlias != null ? keystoreClientAlias.hashCode() : 0);
        result = 31 * result + (truststoreType != null ? truststoreType.hashCode() : 0);
        result = 31 * result + (truststoreProvider != null ? truststoreProvider.hashCode() : 0);
        result = 31 * result + (truststoreLocation != null ? truststoreLocation.hashCode() : 0);
        result = 31 * result + (truststorePassword != null ? truststorePassword.hashCode() : 0);
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(SSLContextConfig config) {
        return new Builder(config);
    }

    public static final class Builder {

        private String protocol = "TLS";
        private String keyManager = KeyManagerFactory.getDefaultAlgorithm();
        private String trustManager = TrustManagerFactory.getDefaultAlgorithm();
        private String keystoreType = KeyStore.getDefaultType();
        private String keystoreProvider = "SUN";
        private String keystoreLocation;
        private String keystorePassword;
        private String keystoreServerAlias;
        private String keystoreClientAlias;
        private String truststoreType = KeyStore.getDefaultType();
        private String truststoreProvider = "SUN";
        private String truststoreLocation;
        private String truststorePassword;

        private Builder() {
        }

        private Builder(SSLContextConfig sslConfig) {
            this.protocol = sslConfig.protocol;
            this.keyManager = sslConfig.keyManager;
            this.trustManager = sslConfig.trustManager;
            this.keystoreType = sslConfig.keystoreType;
            this.keystoreProvider = sslConfig.keystoreProvider;
            this.keystoreLocation = sslConfig.keystoreLocation;
            this.keystorePassword = sslConfig.keystorePassword;
            this.keystoreServerAlias = sslConfig.keystoreServerAlias;
            this.keystoreClientAlias = sslConfig.keystoreClientAlias;
            this.truststoreType = sslConfig.truststoreType;
            this.truststoreProvider = sslConfig.truststoreProvider;
            this.truststoreLocation = sslConfig.truststoreLocation;
            this.truststorePassword = sslConfig.truststorePassword;
        }

        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder keyManager(String keyManager) {
            this.keyManager = keyManager;
            return this;
        }

        public Builder trustManager(String trustManager) {
            this.trustManager = trustManager;
            return this;
        }

        public Builder keystoreType(String keystoreType) {
            this.keystoreType = keystoreType;
            return this;
        }

        public Builder keystoreProvider(String keystoreProvider) {
            this.keystoreProvider = keystoreProvider;
            return this;
        }

        public Builder keystoreLocation(String keystoreLocation) {
            this.keystoreLocation = keystoreLocation;
            return this;
        }

        public Builder keystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
            return this;
        }

        public Builder keystoreServerAlias(String keystoreServerAlias) {
            this.keystoreServerAlias = keystoreServerAlias;
            return this;
        }

        public Builder keystoreClientAlias(String keystoreClientAlias) {
            this.keystoreClientAlias = keystoreClientAlias;
            return this;
        }

        public Builder truststoreType(String truststoreType) {
            this.truststoreType = truststoreType;
            return this;
        }

        public Builder truststoreProvider(String truststoreProvider) {
            this.truststoreProvider = truststoreProvider;
            return this;
        }

        public Builder truststoreLocation(String truststoreLocation) {
            this.truststoreLocation = truststoreLocation;
            return this;
        }

        public Builder truststorePassword(String truststorePassword) {
            this.truststorePassword = truststorePassword;
            return this;
        }

        public SSLContextConfig build() {
            LOGGER.debug("Building SSL Context Config: {}", this);
            return new SSLContextConfig(this);
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "protocol='" + protocol + '\'' +
                    ", keyManager='" + keyManager + '\'' +
                    ", trustManager='" + trustManager + '\'' +
                    ", keystoreType='" + keystoreType + '\'' +
                    ", keystoreProvider='" + keystoreProvider + '\'' +
                    ", keystoreLocation='" + keystoreLocation + '\'' +
                    ", keystorePassword='" + (keystorePassword == null ? null : PASSWORD_MASK) + '\'' +
                    ", keystoreServerAlias='" + keystoreServerAlias + '\'' +
                    ", keystoreClientAlias='" + keystoreClientAlias + '\'' +
                    ", truststoreType='" + truststoreType + '\'' +
                    ", truststoreProvider='" + truststoreProvider + '\'' +
                    ", truststoreLocation='" + truststoreLocation + '\'' +
                    ", truststorePassword='" + (truststorePassword == null ? null : PASSWORD_MASK) + '\'' +
                    '}';
        }
    }

}
