package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserSnapshotCommand(
        UUID userId,
        String displayName,
        Instant updatedAt
) {
    public UpdateUserSnapshotCommand {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
        ObjectPrecondition.requireNonNull(updatedAt, "updatedAt");
    }

    public static UpdateUserSnapshotCommand of(UUID userId, String displayName, Instant updatedAt) {
        return new UpdateUserSnapshotCommand(userId, displayName, updatedAt);
    }
}
