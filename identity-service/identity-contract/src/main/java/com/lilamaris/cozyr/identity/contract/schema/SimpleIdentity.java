package com.lilamaris.cozyr.identity.contract.schema;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.Set;
import java.util.UUID;

public record SimpleIdentity(
        UUID id,
        String displayName,
        Set<Scope> scopes,
        long version
) implements Identity {
    public SimpleIdentity {
        ObjectPrecondition.requireNonNull(id, "id");
        StringPrecondition.requireNonBlank(displayName, "displayName");
        scopes = Set.copyOf(ObjectPrecondition.requireNonNull(scopes, "scopes"));
        NumberPrecondition.requireNonNegative(version, "version");
    }

    public static SimpleIdentity of(UUID id, String displayName, Set<Scope> scopes, long version) {
        return new SimpleIdentity(id, displayName, scopes, version);
    }

    public static SimpleIdentity from(Identity identity) {
        ObjectPrecondition.requireNonNull(identity, "identity");
        return new SimpleIdentity(identity.id(), identity.displayName(), identity.scopes(), identity.version());
    }
}
