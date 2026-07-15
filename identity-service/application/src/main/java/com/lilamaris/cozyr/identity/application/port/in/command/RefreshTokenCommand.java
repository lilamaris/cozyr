package com.lilamaris.cozyr.identity.application.port.in.command;

import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public record RefreshTokenCommand(String refresh) {
    public RefreshTokenCommand {
        StringPrecondition.requireNonBlank(refresh, "refresh");
    }

    public static RefreshTokenCommand of(String refresh) {
        return new RefreshTokenCommand(refresh);
    }
}
