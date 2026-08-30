package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.contract.schema.Scope;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface UserScopeStore {
    boolean tryCreate(UUID userId, List<Scope> scopes, Instant createdAt);
}
