package com.lilamaris.cozyr.identity.security.credential.handler;

import com.lilamaris.cozyr.identity.application.model.token.TokenItem;
import com.lilamaris.cozyr.identity.application.port.in.IssueTokenUseCase;
import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticateResult;
import com.lilamaris.cozyr.kernel.web.response.ServletResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
public class CredentialAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final IssueTokenUseCase issueTokenUseCase;
    private final ServletResponseWriter responseWriter;

    @Override
    public void onAuthenticationSuccess(
            @Nullable HttpServletRequest request,
            @Nullable HttpServletResponse response,
            @Nullable Authentication authentication
    ) throws IOException {
        if (response == null) return;
        if (authentication == null) return;
        if (!(authentication.getPrincipal() instanceof AuthenticateResult result)) return;

        var token = issueTokenUseCase.issue(result.userId());

        var accessTokenCookie = buildTokenCookie("access_token", token.accessToken());
        var refreshTokenCookie = buildTokenCookie("refresh_token", token.refreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        responseWriter.noContent(response);
    }

    private ResponseCookie buildTokenCookie(String name, TokenItem tokenItem) {
        var value = tokenItem.value();
        var expiresIn = tokenItem.expiresIn();
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(expiresIn)
                .sameSite("Lax")
                .build();
    }
}
