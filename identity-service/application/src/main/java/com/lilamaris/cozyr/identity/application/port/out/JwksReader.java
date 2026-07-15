package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.application.model.RSAKeyPair;
import com.lilamaris.cozyr.identity.domain.RSAKey;

import java.util.List;

public interface JwksReader {
    RSAKeyPair findSignableKey();

    List<RSAKey> findVerifiableKeys();
}
