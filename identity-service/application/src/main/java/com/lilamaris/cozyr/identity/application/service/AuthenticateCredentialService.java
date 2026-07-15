package com.lilamaris.cozyr.identity.application.service;

import com.lilamaris.cozyr.identity.application.exception.IdentityServiceProgressCode;
import com.lilamaris.cozyr.identity.application.port.in.AuthenticateCredentialUseCase;
import com.lilamaris.cozyr.identity.application.port.in.command.AuthenticateCredentialCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticatedResult;
import com.lilamaris.cozyr.identity.application.port.out.CredentialReader;
import com.lilamaris.cozyr.identity.application.port.out.PrincipalReader;
import com.lilamaris.cozyr.identity.application.port.out.UserReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateCredentialService implements AuthenticateCredentialUseCase {
    private final CredentialReader credentialReader;
    private final UserReader userReader;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateCredentialService(
            CredentialReader credentialReader,
            UserReader userReader,
            PasswordEncoder passwordEncoder
    ) {
        this.credentialReader = credentialReader;
        this.userReader = userReader;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthenticatedResult authenticate(AuthenticateCredentialCommand command) {
        var email = command.email();
        var credential = credentialReader.findByEmail(email)
                .orElseThrow(() -> new ApplicationException(IdentityServiceProgressCode.ACCOUNT_NOT_FOUND));

        var password = command.password();
        if (!passwordEncoder.matches(password, credential.getPasswordHash())) throw new ApplicationException(IdentityServiceProgressCode.AUTHENTICATE_FAILED);

        var user = userReader.findById(credential.getUserId())
                .orElseThrow(() -> new ApplicationException(IdentityServiceProgressCode.USER_NOT_FOUND));

        return AuthenticatedResult.of(user.getId(), user.getDisplayName());
    }
}
