package xyz.baddeveloper.lwsl.ssl;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedKeyManager;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * This class is used if we have custom server or client aliases set, in order to select the correct alias.
 */
public class CustomKeyManager extends X509ExtendedKeyManager {

    private final X509ExtendedKeyManager originalKeyManager;
    private final String serverAlias;
    private final String clientAlias;

    public CustomKeyManager(X509ExtendedKeyManager originalKeyManager, String serverAlias, String clientAlias) {
        this.originalKeyManager = originalKeyManager;
        this.serverAlias = serverAlias;
        this.clientAlias = clientAlias;
    }

    @Override
    public String[] getClientAliases(String keyType, Principal[] principals) {
        return originalKeyManager.getClientAliases(keyType, principals);
    }

    @Override
    public String chooseClientAlias(String[] keyTypes, Principal[] principals, Socket socket) {
        if (this.clientAlias != null) {
            return this.clientAlias;
        } else {
            return originalKeyManager.chooseClientAlias(keyTypes, principals, socket);
        }
    }

    @Override
    public String[] getServerAliases(String keyType, Principal[] principals) {
        return originalKeyManager.getServerAliases(keyType, principals);
    }

    @Override
    public String chooseServerAlias(String keyType, Principal[] principals, Socket socket) {
        if (this.serverAlias != null) {
            return this.serverAlias;
        } else {
            return originalKeyManager.chooseServerAlias(keyType, principals, socket);
        }
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        return originalKeyManager.getCertificateChain(alias);
    }

    @Override
    public PrivateKey getPrivateKey(String alias) {
        return originalKeyManager.getPrivateKey(alias);
    }

    @Override
    public String chooseEngineClientAlias(String[] keyType, Principal[] principals, SSLEngine sslEngine) {
        if (this.clientAlias != null) {
            return this.clientAlias;
        } else {
            return originalKeyManager.chooseEngineClientAlias(keyType, principals, sslEngine);
        }
    }

    @Override
    public String chooseEngineServerAlias(String keyType, Principal[] principals, SSLEngine sslEngine) {
        if (this.serverAlias != null) {
            return this.serverAlias;
        } else {
            return originalKeyManager.chooseEngineServerAlias(keyType, principals, sslEngine);
        }
    }

}
