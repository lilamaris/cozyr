package com.lilamaris.cozyr.identity.security.credential.handler;

import com.lilamaris.cozyr.identity.application.port.in.IssueTokenUseCase;
import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticatedResult;
import com.lilamaris.cozyr.kernel.web.response.ServletResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
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
        if (authentication == null) return;
        if (!(authentication.getPrincipal() instanceof AuthenticatedResult result)) return;

        var token = issueTokenUseCase.issue(result);

        responseWriter.write(response, HttpStatus.OK, token);
    }
}
