package com.lilamaris.cozyr.identity.application.port.in.result;

public record TokenResult(
        String access,
        String refresh
) {
    public static TokenResult of(String access, String refresh) {
        return new TokenResult(access, refresh);
    }
}
