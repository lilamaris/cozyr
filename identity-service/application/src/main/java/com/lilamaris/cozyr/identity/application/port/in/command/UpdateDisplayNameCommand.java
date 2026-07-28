package com.lilamaris.cozyr.identity.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record UpdateDisplayNameCommand(
        UUID userId,
        String displayName
) {
    public UpdateDisplayNameCommand {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(displayName, "displayName");
    }

    public static UpdateDisplayNameCommand of(UUID userId, String displayName) {
        return new UpdateDisplayNameCommand(userId, displayName);
    }
}
