package com.lilamaris.cozyr.reservation.kafka;

import com.lilamaris.cozyr.identity.contract.event.UserCreatedEvent;
import com.lilamaris.cozyr.identity.contract.event.UserUpdatedEvent;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.reservation.application.port.in.CreateUserSnapshotUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.UpdateUserSnapshotUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.CreateUserSnapshotCommand;
import com.lilamaris.cozyr.reservation.application.port.in.command.UpdateUserSnapshotCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdentityServiceMessageListener {
    private final CreateUserSnapshotUseCase createUserSnapshotUseCase;
    private final UpdateUserSnapshotUseCase updateUserSnapshotUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "user.created",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void handleUserCreated(MessageEnvelope<?> message, Acknowledgment ack) {
        log.info("Handle User Created Message: {}", message);

        var event = objectMapper.convertValue(message.payload(), UserCreatedEvent.class);
        var command = CreateUserSnapshotCommand.of(event.userId(), event.displayName());
        createUserSnapshotUseCase.create(command);

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "user.updated",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void handleUserUpdated(MessageEnvelope<?> message, Acknowledgment ack) {
        log.info("Handle User Updated Message: {}", message);

        var event = objectMapper.convertValue(message.payload(), UserUpdatedEvent.class);
        var command = UpdateUserSnapshotCommand.of(event.userId(), event.displayName(), event.updatedAt());
        updateUserSnapshotUseCase.update(command);

        ack.acknowledge();
    }
}
