package com.lilamaris.cozyr.identity.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record UpdateCredentialCommand(
        UUID userId,
        String password
) {
    public UpdateCredentialCommand {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(password, "password");
    }

    public static UpdateCredentialCommand of(UUID userId, String password) {
        return new UpdateCredentialCommand(userId, password);
    }
}
