package com.lilamaris.cozyr.identity.contract.schema;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record SimpleIdentity(
        UUID id,
        String displayName
) implements Identity {
    public SimpleIdentity {
        ObjectPrecondition.requireNonNull(id, "id");
        StringPrecondition.requireNonBlank(displayName, "displayName");
    }

    public static SimpleIdentity of(UUID id, String displayName) {
        return new SimpleIdentity(id, displayName);
    }

    public static SimpleIdentity from(Identity identity) {
        ObjectPrecondition.requireNonNull(identity, "identity");
        return new SimpleIdentity(identity.id(), identity.displayName());
    }
}
