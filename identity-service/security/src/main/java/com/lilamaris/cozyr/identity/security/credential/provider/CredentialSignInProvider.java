package com.lilamaris.cozyr.identity.security.credential.provider;

import com.lilamaris.cozyr.identity.application.port.in.AuthenticateCredentialUseCase;
import com.lilamaris.cozyr.identity.security.credential.token.CredentialAuthenticateToken;
import com.lilamaris.cozyr.kernel.security.exception.ApplicationAuthenticationException;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class CredentialSignInProvider implements AuthenticationProvider {
    private final AuthenticateCredentialUseCase authenticateCredentialUseCase;

    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        var token = (CredentialAuthenticateToken) authentication;
        var command = token.getCommand();

        try {
            var auth = authenticateCredentialUseCase.authenticate(command);
            return CredentialAuthenticateToken.of(auth);
        } catch (ApplicationException e) {
            throw new ApplicationAuthenticationException(e.getApplicationCode(), e);
        }
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return CredentialAuthenticateToken.class.isAssignableFrom(authentication);
    }
}
