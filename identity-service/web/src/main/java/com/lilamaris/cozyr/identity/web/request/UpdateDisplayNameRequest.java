package com.lilamaris.cozyr.identity.web.request;

import com.lilamaris.cozyr.identity.application.port.in.command.UpdateDisplayNameCommand;

import java.util.UUID;

public record UpdateDisplayNameRequest(
        String displayName
) {
    public UpdateDisplayNameCommand toCommand(UUID userId) {
        return UpdateDisplayNameCommand.of(userId, displayName);
    }
}
