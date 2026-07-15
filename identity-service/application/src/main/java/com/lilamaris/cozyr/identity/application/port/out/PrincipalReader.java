package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.application.model.AuthenticatedPrincipal;

import java.util.Optional;
import java.util.UUID;

public interface PrincipalReader {
    Optional<AuthenticatedPrincipal> findByUserId(UUID userId);
}
