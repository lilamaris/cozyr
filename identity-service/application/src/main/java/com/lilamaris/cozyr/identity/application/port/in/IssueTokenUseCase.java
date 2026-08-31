package com.lilamaris.cozyr.identity.application.port.in;

import com.lilamaris.cozyr.identity.application.port.in.result.TokenResult;

import java.util.UUID;

public interface IssueTokenUseCase {
    TokenResult issue(UUID userId);
}
