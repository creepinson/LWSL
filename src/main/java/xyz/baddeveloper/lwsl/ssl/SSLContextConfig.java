package xyz.baddeveloper.lwsl.ssl;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;
import java.util.Objects;

public class SSLContextConfig {

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SSLContextConfig that = (SSLContextConfig) o;
        return Objects.equals(protocol, that.protocol) &&
                Objects.equals(keyManager, that.keyManager) &&
                Objects.equals(trustManager, that.trustManager) &&
                Objects.equals(keystoreType, that.keystoreType) &&
                Objects.equals(keystoreProvider, that.keystoreProvider) &&
                Objects.equals(keystoreLocation, that.keystoreLocation) &&
                Objects.equals(keystorePassword, that.keystorePassword) &&
                Objects.equals(keystoreServerAlias, that.keystoreServerAlias) &&
                Objects.equals(keystoreClientAlias, that.keystoreClientAlias) &&
                Objects.equals(truststoreType, that.truststoreType) &&
                Objects.equals(truststoreProvider, that.truststoreProvider) &&
                Objects.equals(truststoreLocation, that.truststoreLocation) &&
                Objects.equals(truststorePassword, that.truststorePassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocol, keyManager, trustManager, keystoreType, keystoreProvider, keystoreLocation, keystorePassword, keystoreServerAlias, keystoreClientAlias, truststoreType, truststoreProvider, truststoreLocation, truststorePassword);
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
