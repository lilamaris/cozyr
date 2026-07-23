package com.lilamaris.cozyr.identity.application.model;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import org.jspecify.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public record RSAKeyPair(
        String kid,
        RSAPublicKey publicKey,
        @Nullable RSAPrivateKey privateKey
) {
    public RSAKeyPair {
        StringPrecondition.requireNonBlank(kid, "kid");
        ObjectPrecondition.requireNonNull(publicKey, "publicKey");
        if (privateKey != null) {
            try {
                validateKeyPair(kid, publicKey, privateKey);
            } catch (GeneralSecurityException e) {
                throw new IllegalStateException("failed to validate key pair.", e);
            }
        }
    }

    public static RSAKeyPair signable(String kid, RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        ObjectPrecondition.requireNonNull(privateKey, "privateKey");
        return new RSAKeyPair(kid, publicKey, privateKey);
    }

    public static RSAKeyPair verifiable(String kid, RSAPublicKey publicKey) {
        return new RSAKeyPair(kid, publicKey, null);
    }

    public RSAKeyPair toVerifiable() {
        return RSAKeyPair.verifiable(kid, publicKey);
    }

    private void validateKeyPair(String kid, RSAPublicKey publicKey, RSAPrivateKey privateKey) throws GeneralSecurityException {
        if (privateKey == null) throw new IllegalStateException("private key not exists. kid=" + kid);
        var plain = "key-pair-check-message".getBytes(StandardCharsets.UTF_8);
        var signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(plain);
        var signature = signer.sign();

        var verifier = Signature.getInstance("SHA256withRSA");
        verifier.initVerify(publicKey);
        verifier.update(plain);
        if (!verifier.verify(signature)) throw new IllegalStateException("public key does not match private key. kid=" + kid);
    }
}
