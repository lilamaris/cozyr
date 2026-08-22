package com.lilamaris.cozyr.reservation.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record CreateUserSnapshotCommand(
        UUID userId,
        String displayName
) {
    public CreateUserSnapshotCommand {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
    }

    public static CreateUserSnapshotCommand of(UUID userId, String displayName) {
        return new CreateUserSnapshotCommand(userId, displayName);
    }
}
