package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.port.in.command.RegisterCredentialCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticateResult;

public interface RegisterCredentialUseCase {
    AuthenticateResult register(RegisterCredentialCommand command);
}
