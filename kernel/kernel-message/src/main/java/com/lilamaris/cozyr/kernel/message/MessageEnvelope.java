package com.lilamaris.cozyr.kernel.message;

import com.lilamaris.cozyr.kernel.core.condition.NumberPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

public record MessageEnvelope<T>(
        String kind,
        int version,
        @Nullable String key,
        T payload,
        Instant createdAt
) {
    public MessageEnvelope {
        StringPrecondition.requireNonBlank(kind, "kind");
        NumberPrecondition.requirePositive(version, "version");
        ObjectPrecondition.requireNonNull(payload, "payload");
        ObjectPrecondition.requireNonNull(createdAt, "createdAt");

        if (key != null) {
            StringPrecondition.requireNonBlank(key, "key");
        }
    }

    public static <T extends MessagePayload> MessageEnvelope<T> of(@Nullable String key, T payload, Instant createdAt) {
        var kind = ObjectPrecondition.requireNonNull(payload, "payload").kind();
        return of(kind.canonicalName(), kind.version(), key, payload, createdAt);
    }

    public static <T> MessageEnvelope<T> of(
            String kind,
            int version,
            @Nullable String key,
            T payload,
            Instant createdAt
    ) {
        return new MessageEnvelope<>(kind, version, key, payload, createdAt);
    }
}
