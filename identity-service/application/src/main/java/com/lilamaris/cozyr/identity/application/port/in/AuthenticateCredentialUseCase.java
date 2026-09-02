package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.port.in.command.AuthenticateCredentialCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticateResult;

public interface AuthenticateCredentialUseCase {
    AuthenticateResult authenticate(AuthenticateCredentialCommand command);
}
