package com.lilamaris.cozyr.identity.domain;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import lombok.Getter;

import java.security.PrivateKey;
import java.security.PublicKey;

@Getter
public abstract class Key {
    private final String kid;
    private final KeyType keyType;
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    protected Key(String kid, KeyType keyType, PublicKey publicKey, PrivateKey privateKey) {
        this.kid = StringPrecondition.requireNonBlank(kid, "kid");
        this.keyType = ObjectPrecondition.requireNonNull(keyType, "keyType");
        this.publicKey = ObjectPrecondition.requireNonNull(publicKey, "publicKey");

        if (keyType == KeyType.SIGNABLE) {
            this.privateKey = ObjectPrecondition.requireNonNull(privateKey, "privateKey");
        } else {
            this.privateKey = null;
        }
    }

    public boolean isSignable() {
        return keyType == KeyType.SIGNABLE;
    }
}
