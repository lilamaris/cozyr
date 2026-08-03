package com.lilamaris.cozyr.identity.application.service;

import com.lilamaris.cozyr.identity.application.exception.IdentityServiceProgressCode;
import com.lilamaris.cozyr.identity.application.port.in.UpdateDisplayNameUseCase;
import com.lilamaris.cozyr.identity.application.port.in.command.UpdateDisplayNameCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.UpdatedDisplayNameResult;
import com.lilamaris.cozyr.identity.application.port.out.UserReader;
import com.lilamaris.cozyr.identity.contract.event.UserUpdatedEvent;
import com.lilamaris.cozyr.kernel.message.MessagePublisher;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class UpdateDisplayNameService implements UpdateDisplayNameUseCase {
    private final UserReader reader;
    private final MessagePublisher messagePublisher;
    private final Clock clock;

    @Override
    @Transactional
    public UpdatedDisplayNameResult update(UpdateDisplayNameCommand command) {
        var userId = command.userId();
        var user = reader.findById(userId)
                .orElseThrow(() -> new ApplicationException(IdentityServiceProgressCode.USER_NOT_FOUND));

        var now = clock.instant();
        var displayName = command.displayName();
        user.updateDisplayName(displayName, now);

        var payload = UserUpdatedEvent.of(userId, displayName, now);
        messagePublisher.publish(payload.toMessage(now));

        return UpdatedDisplayNameResult.from(user);
    }
}
