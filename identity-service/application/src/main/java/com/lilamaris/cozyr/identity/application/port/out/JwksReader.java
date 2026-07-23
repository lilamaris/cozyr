package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.application.model.RSAKeyPair;

import java.util.List;

public interface JwksReader {
    RSAKeyPair findSignableKey();

    List<RSAKeyPair> findVerifiableKeys();
}
