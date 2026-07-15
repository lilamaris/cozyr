package com.lilamaris.cozyr.identity.application.model;

import com.lilamaris.cozyr.identity.contract.schema.Scope;
import com.lilamaris.cozyr.kernel.core.condition.CollectionPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.Set;
import java.util.UUID;

public record AuthenticatedPrincipal(
        UUID userId,
        String displayName,
        Set<Scope> scopes
) {
    public AuthenticatedPrincipal {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
        CollectionPrecondition.requireNonNullElements(scopes, "scopes");
    }

    public static AuthenticatedPrincipal of(UUID userId, String displayName, Set<Scope> scopes) {
        return new AuthenticatedPrincipal(userId, displayName, scopes);
    }
}
