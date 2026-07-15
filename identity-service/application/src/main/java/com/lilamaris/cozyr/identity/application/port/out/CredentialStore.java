package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.domain.Credential;

public interface CredentialStore {
    Credential save(Credential credential);
}
