package com.lilamaris.cozyr.identity.web.request;

import com.lilamaris.cozyr.identity.application.port.in.command.UpdatePasswordCommand;

import java.util.UUID;

public record UpdatePasswordRequest(
        String originalPassword,
        String newPassword
) {
    public UpdatePasswordCommand toCommand(UUID userId) {
        return UpdatePasswordCommand.of(userId, originalPassword, newPassword);
    }
}
