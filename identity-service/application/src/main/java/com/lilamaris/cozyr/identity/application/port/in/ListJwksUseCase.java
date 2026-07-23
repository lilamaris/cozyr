package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.model.RSAKeyPair;

import java.util.List;

public interface ListJwksUseCase {
    List<RSAKeyPair> list();
}
