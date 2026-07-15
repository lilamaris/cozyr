package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.port.in.command.RegisterCredentialCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticatedResult;

public interface RegisterCredentialUseCase {
    AuthenticatedResult register(RegisterCredentialCommand command);
}
