package com.lilamaris.cozyr.identity.security.credential.token;

import com.lilamaris.cozyr.identity.application.port.in.command.RegisterCredentialCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticateResult;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

@Getter
public class CredentialRegisterToken extends AbstractAuthenticationToken {
    private RegisterCredentialCommand command;
    private AuthenticateResult result;

    private CredentialRegisterToken(RegisterCredentialCommand command) {
        super(List.of());
        setAuthenticated(false);

        this.command = ObjectPrecondition.requireNonNull(command, "command");
    }

    private CredentialRegisterToken(AuthenticateResult result) {
        super(List.of());
        super.setAuthenticated(true);

        this.result = ObjectPrecondition.requireNonNull(result, "result");
    }

    public static CredentialRegisterToken of(RegisterCredentialCommand command) {
        return new CredentialRegisterToken(command);
    }

    public static CredentialRegisterToken of(AuthenticateResult result) {
        return new CredentialRegisterToken(result);
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
