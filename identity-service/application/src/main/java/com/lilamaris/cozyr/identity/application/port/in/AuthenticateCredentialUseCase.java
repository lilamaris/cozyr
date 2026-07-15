package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.port.in.command.AuthenticateCredentialCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticatedResult;

public interface AuthenticateCredentialUseCase {
    AuthenticatedResult authenticate(AuthenticateCredentialCommand command);
}
