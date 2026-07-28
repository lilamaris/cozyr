package com.lilamaris.cozyr.identity.jpa.row;

import com.lilamaris.cozyr.identity.contract.schema.Role;
import com.lilamaris.cozyr.identity.contract.schema.Scope;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public record PrincipalRow(
        UUID userId,
        String displayName,
        long version,
        @Nullable String service,
        @Nullable String roleName
) {
    public Optional<Scope> toScope() {
        if (service == null || roleName == null) return Optional.empty();

        return Optional.of(
                Scope.of(service, Role.from(roleName))
        );
    }
}
