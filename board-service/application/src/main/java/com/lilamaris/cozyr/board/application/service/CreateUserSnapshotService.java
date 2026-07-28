package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.port.in.CreateUserSnapshotUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.CreateUserSnapshotCommand;
import com.lilamaris.cozyr.board.application.port.out.UserSnapshotStore;
import com.lilamaris.cozyr.board.domain.UserSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CreateUserSnapshotService implements CreateUserSnapshotUseCase {
    private final UserSnapshotStore store;
    private final Clock clock;

    @Override
    @Transactional
    public void create(CreateUserSnapshotCommand command) {
        var now = clock.instant();
        var userSnapshot = UserSnapshot.of(command.userId(), command.displayName(), now);
        store.upsert(userSnapshot);
    }
}
