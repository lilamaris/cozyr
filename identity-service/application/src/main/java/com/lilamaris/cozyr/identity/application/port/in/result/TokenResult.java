package com.lilamaris.cozyr.identity.application.port.in.result;

import com.lilamaris.cozyr.identity.application.model.token.TokenItem;

public record TokenResult(
        TokenItem accessToken,
        TokenItem refreshToken
) {
    public static TokenResult of(TokenItem accessToken, TokenItem refreshToken) {
        return new TokenResult(accessToken, refreshToken);
    }
}
