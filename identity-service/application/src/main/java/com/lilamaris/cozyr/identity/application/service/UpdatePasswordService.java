package com.lilamaris.cozyr.identity.application.service;

import com.lilamaris.cozyr.identity.application.exception.IdentityServiceProgressCode;
import com.lilamaris.cozyr.identity.application.port.in.UpdatePasswordUseCase;
import com.lilamaris.cozyr.identity.application.port.in.command.UpdatePasswordCommand;
import com.lilamaris.cozyr.identity.application.port.out.CredentialReader;
import com.lilamaris.cozyr.identity.application.port.out.UserReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class UpdatePasswordService implements UpdatePasswordUseCase {
    private final UserReader userReader;
    private final CredentialReader credentialReader;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Override
    @Transactional
    public void update(UpdatePasswordCommand command) {
        var userId = command.userId();
        if (!userReader.existsById(userId))
            throw new ApplicationException(IdentityServiceProgressCode.USER_NOT_FOUND);

        var credential = credentialReader.findByUserId(userId)
                .orElseThrow(() -> new ApplicationException(IdentityServiceProgressCode.ACCOUNT_NOT_FOUND));

        var originalPassword = command.originalPassword();
        if (!passwordEncoder.matches(originalPassword, credential.getPasswordHash()))
            throw new ApplicationException(IdentityServiceProgressCode.AUTHENTICATE_FAILED);

        var now = clock.instant();
        var newPassword = command.newPassword();
        var passwordHash = passwordEncoder.encode(newPassword);

        credential.updatePasswordHash(passwordHash, now);
    }
}
