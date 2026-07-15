package com.lilamaris.cozyr.identity.security.credential.request;

import com.lilamaris.cozyr.identity.application.port.in.command.AuthenticateCredentialCommand;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

public record CredentialAuthenticateRequest(
        String email,
        String password
) {
    public CredentialAuthenticateRequest {
        StringPrecondition.requireNonBlank(email, "email");
        StringPrecondition.requireNonBlank(password, "password");
    }

    public AuthenticateCredentialCommand toCommand() {
        return AuthenticateCredentialCommand.of(email, password);
    }
}
