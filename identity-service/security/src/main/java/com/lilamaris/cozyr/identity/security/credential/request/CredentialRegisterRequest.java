package com.lilamaris.cozyr.identity.security.credential.request;

import com.lilamaris.cozyr.identity.application.port.in.command.RegisterCredentialCommand;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public record CredentialRegisterRequest(
        String displayName,
        String email,
        String password
) {
    public CredentialRegisterRequest {
        StringPrecondition.requireNonBlank(displayName, "displayName");
        StringPrecondition.requireNonBlank(email, "email");
        StringPrecondition.requireNonBlank(password, "password");
    }

    public RegisterCredentialCommand toCommand() {
        return RegisterCredentialCommand.of(displayName, email, password);
    }
}
