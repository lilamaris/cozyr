package com.lilamaris.cozyr.identity.security.credential.provider;

import com.lilamaris.cozyr.identity.application.exception.IdentityServiceProgressCode;
import com.lilamaris.cozyr.identity.application.port.in.RegisterCredentialUseCase;
import com.lilamaris.cozyr.identity.security.credential.token.CredentialRegisterToken;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class CredentialSignUpProvider implements AuthenticationProvider {
    private final RegisterCredentialUseCase registerCredentialUseCase;

    @Override
    public @Nullable Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        var token = (CredentialRegisterToken) authentication;
        var command = token.getCommand();

        try {
            var auth = registerCredentialUseCase.register(command);
            return CredentialRegisterToken.of(auth);
        } catch (ApplicationException e) {
            var errorCode = (IdentityServiceProgressCode) e.getApplicationCode();
            throw switch (errorCode) {
                case AUTHENTICATE_FAILED, EMAIL_DUPLICATED -> new AuthenticationServiceException(e.getMessage());
                default -> new AuthenticationServiceException("Credential sign up failed", e);
            };
        }
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return CredentialRegisterToken.class.isAssignableFrom(authentication);
    }
}
