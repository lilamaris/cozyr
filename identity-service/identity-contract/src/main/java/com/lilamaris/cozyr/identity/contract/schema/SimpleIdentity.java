package com.lilamaris.cozyr.identity.contract.schema;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record SimpleIdentity(
        UUID id,
        String displayName,
        long version
) implements Identity {
    public SimpleIdentity {
        ObjectPrecondition.requireNonNull(id, "id");
        StringPrecondition.requireNonBlank(displayName, "displayName");
        NumberPrecondition.requireNonNegative(version, "version");
    }

    public static SimpleIdentity of(UUID id, String displayName, long version) {
        return new SimpleIdentity(id, displayName, version);
    }

    public static SimpleIdentity from(Identity identity) {
        ObjectPrecondition.requireNonNull(identity, "identity");
        return new SimpleIdentity(identity.id(), identity.displayName(), identity.version());
    }
}
