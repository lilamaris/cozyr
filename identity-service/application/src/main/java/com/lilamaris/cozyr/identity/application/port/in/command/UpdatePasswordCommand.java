package com.lilamaris.cozyr.identity.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.util.UUID;

public record UpdatePasswordCommand(
        UUID userId,
        String originalPassword,
        String newPassword
) {
    public UpdatePasswordCommand {
        ObjectPrecondition.requireNonNull(userId, "userId");
        StringPrecondition.requireNonBlank(originalPassword, "originalPassword");
        StringPrecondition.requireNonBlank(newPassword, "newPassword");
    }

    public static UpdatePasswordCommand of(UUID userId, String originalPassword, String newPassword) {
        return new UpdatePasswordCommand(userId, originalPassword, newPassword);
    }
}
