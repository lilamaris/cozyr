package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticatedResult;
import com.lilamaris.cozyr.identity.application.port.in.result.TokenResult;

public interface IssueTokenUseCase {
    TokenResult issue(AuthenticatedResult result);
}
