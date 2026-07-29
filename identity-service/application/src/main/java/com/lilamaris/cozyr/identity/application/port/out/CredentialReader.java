package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.domain.Credential;

import java.util.Optional;
import java.util.UUID;

public interface CredentialReader {
    Optional<Credential> findById(UUID id);

    Optional<Credential> findByUserId(UUID userId);

    Optional<Credential> findByEmail(String email);

    boolean existsByEmail(String email);
}
