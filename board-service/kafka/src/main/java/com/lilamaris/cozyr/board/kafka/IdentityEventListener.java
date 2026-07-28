package com.lilamaris.cozyr.board.kafka;

import com.lilamaris.cozyr.board.application.port.in.CreateUserSnapshotUseCase;
import com.lilamaris.cozyr.board.application.port.in.UpdateUserSnapshotUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.CreateUserSnapshotCommand;
import com.lilamaris.cozyr.board.application.port.in.command.UpdateUserSnapshotCommand;
import com.lilamaris.cozyr.identity.contract.event.UserCreatedEvent;
import com.lilamaris.cozyr.identity.contract.event.UserUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdentityEventListener {
    private final CreateUserSnapshotUseCase createUserSnapshotUseCase;
    private final UpdateUserSnapshotUseCase updateUserSnapshotUseCase;

    @KafkaListener(
            topics = "user.created",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void handleUserCreated(UserCreatedEvent event, Acknowledgment ack) {
        log.info("Handle User Created Event Body: {}", event);

        var command = CreateUserSnapshotCommand.of(event.userId(), event.displayName());
        createUserSnapshotUseCase.create(command);

        ack.acknowledge();
    }

    @KafkaListener(
            topics = "user.updated",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void handleUserUpdated(UserUpdatedEvent event, Acknowledgment ack) {
        log.info("Handle User Updated Event Body: {}", event);

        var command = UpdateUserSnapshotCommand.of(event.userId(), event.displayName(), event.updatedAt());
        updateUserSnapshotUseCase.update(command);

        ack.acknowledge();
    }
}
