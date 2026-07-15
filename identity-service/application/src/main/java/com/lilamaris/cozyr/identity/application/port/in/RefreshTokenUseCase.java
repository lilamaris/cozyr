package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.port.in.command.RefreshTokenCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.TokenResult;

public interface RefreshTokenUseCase {
    TokenResult refresh(RefreshTokenCommand command);
}
