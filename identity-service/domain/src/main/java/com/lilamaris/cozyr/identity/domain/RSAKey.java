package com.lilamaris.cozyr.identity.domain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAKey extends Key {
    protected RSAKey(String kid, KeyType keyType, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        super(kid, keyType, publicKey, privateKey);
    }

    public static RSAKey verifiable(String kid, RSAPublicKey publicKey) {
        return new RSAKey(kid, KeyType.VERIFIABLE, publicKey, null);
    }

    public static RSAKey signable(String kid, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        return new RSAKey(kid, KeyType.SIGNABLE, publicKey, privateKey);
    }
}
