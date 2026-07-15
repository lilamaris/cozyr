package com.lilamaris.cozyr.identity.security.credential.filter;

import com.lilamaris.cozyr.identity.security.credential.request.CredentialRegisterRequest;
import com.lilamaris.cozyr.identity.security.credential.token.CredentialRegisterToken;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonCredentialSignUpProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;

    public JsonCredentialSignUpProcessingFilter(RequestMatcher matcher, ObjectMapper objectMapper) {
        super(matcher);
        this.objectMapper = objectMapper;
    }

    @Override
    public @Nullable Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response
    ) throws AuthenticationException, IOException, ServletException {
        final CredentialRegisterToken token;
        try {
            var body = objectMapper.readValue(request.getInputStream(), CredentialRegisterRequest.class);
            token = CredentialRegisterToken.of(body.toCommand());
        } catch (Exception e) {
            throw new AuthenticationServiceException("Invalid credential sign-up request.");
        }

        return getAuthenticationManager().authenticate(token);
    }
}
