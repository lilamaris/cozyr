package com.lilamaris.cozyr.identity.security.credential.token;

import com.lilamaris.cozyr.identity.application.model.AuthenticatedPrincipal;
import com.lilamaris.cozyr.identity.application.port.in.command.AuthenticateCredentialCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticatedResult;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

@Getter
public class CredentialAuthenticateToken extends AbstractAuthenticationToken {
    private AuthenticateCredentialCommand command;
    private AuthenticatedResult result;

    private CredentialAuthenticateToken(AuthenticateCredentialCommand command) {
        super(List.of());
        setAuthenticated(false);

        this.command = ObjectPrecondition.requireNonNull(command, "command");
    }

    private CredentialAuthenticateToken(AuthenticatedResult result) {
        super(List.of());
        super.setAuthenticated(true);

        this.result = ObjectPrecondition.requireNonNull(result, "result");
    }

    public static CredentialAuthenticateToken of(AuthenticateCredentialCommand command) {
        return new CredentialAuthenticateToken(command);
    }

    public static CredentialAuthenticateToken of(AuthenticatedResult result) {
        return new CredentialAuthenticateToken(result);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.command = null;
    }

    @Override
    public Object getPrincipal() {
        return result;
    }

    @Override
    public Object getCredentials() {
        return command;
    }
}
