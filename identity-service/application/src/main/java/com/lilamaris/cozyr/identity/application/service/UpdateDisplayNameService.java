package com.lilamaris.cozyr.identity.application.service;

import com.lilamaris.cozyr.identity.application.exception.IdentityServiceProgressCode;
import com.lilamaris.cozyr.identity.application.port.in.UpdateDisplayNameUseCase;
import com.lilamaris.cozyr.identity.application.port.in.command.UpdateDisplayNameCommand;
import com.lilamaris.cozyr.identity.application.port.in.result.UpdatedDisplayNameResult;
import com.lilamaris.cozyr.identity.application.port.out.EventPublisher;
import com.lilamaris.cozyr.identity.application.port.out.UserReader;
import com.lilamaris.cozyr.identity.contract.event.EventType;
import com.lilamaris.cozyr.identity.contract.event.UserUpdatedEvent;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
public class UpdateDisplayNameService implements UpdateDisplayNameUseCase {
    private final UserReader reader;
    private final EventPublisher eventPublisher;
    private final Clock clock;

    public UpdateDisplayNameService(UserReader reader, EventPublisher eventPublisher, Clock clock) {
        this.reader = reader;
        this.eventPublisher = eventPublisher;
        this.clock = clock;
    }

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
        eventPublisher.publish(EventType.USER_UPDATED, payload);

        return UpdatedDisplayNameResult.from(user);
    }
}
