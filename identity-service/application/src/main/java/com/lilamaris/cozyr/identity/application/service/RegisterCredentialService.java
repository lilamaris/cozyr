package com.lilamaris.cozyr.identity.application.service;

import com.lilamaris.cozyr.identity.application.exception.IdentityServiceProgressCode;
import com.lilamaris.cozyr.identity.application.port.in.RegisterCredentialUseCase;
import com.lilamaris.cozyr.identity.application.port.in.command.RegisterCredentialCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.AuthenticatedResult;
import com.lilamaris.cozyr.identity.application.port.out.CredentialReader;
import com.lilamaris.cozyr.identity.application.port.out.CredentialStore;
import com.lilamaris.cozyr.identity.application.port.out.EventPublisher;
import com.lilamaris.cozyr.identity.application.port.out.UserStore;
import com.lilamaris.cozyr.identity.contract.event.EventType;
import com.lilamaris.cozyr.identity.contract.event.UserCreatedEvent;
import com.lilamaris.cozyr.identity.domain.Credential;
import com.lilamaris.cozyr.identity.domain.User;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class RegisterCredentialService implements RegisterCredentialUseCase {
    private final CredentialStore credentialStore;
    private final UserStore userStore;
    private final CredentialReader credentialReader;
    private final EventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    public RegisterCredentialService(
            CredentialStore credentialStore,
            UserStore userStore,
            CredentialReader credentialReader,
            EventPublisher eventPublisher,
            PasswordEncoder passwordEncoder,
            Clock clock
    ) {
        this.credentialStore = credentialStore;
        this.userStore = userStore;
        this.credentialReader = credentialReader;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    @Override
    @Transactional
    public AuthenticatedResult register(RegisterCredentialCommand command) {
        var email = command.email();
        var exists = credentialReader.existsByEmail(email);
        if (exists) throw new ApplicationException(IdentityServiceProgressCode.EMAIL_DUPLICATED);

        var now = clock.instant();
        var displayName = command.displayName();
        var user = User.of(displayName, now);
        var savedUser = userStore.save(user);
        var userId = savedUser.getId();

        var password = command.password();
        var passwordHash = passwordEncoder.encode(password);
        var credential = Credential.of(userId, email, passwordHash, now);
        credentialStore.save(credential);

        var payload = UserCreatedEvent.of(userId, savedUser.displayName(), now);
        eventPublisher.publish(EventType.USER_CREATED, payload);

        return AuthenticatedResult.of(userId, displayName);
    }
}
