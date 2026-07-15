package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.domain.UserScope;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserScopeReader {
    Optional<UserScope> findById(UUID id);

    Set<UserScope> findByUserId(UUID userId);
}
