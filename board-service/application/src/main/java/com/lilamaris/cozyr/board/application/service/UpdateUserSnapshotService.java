package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.port.in.UpdateUserSnapshotUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.UpdateUserSnapshotCommand;
import com.lilamaris.cozyr.board.application.port.out.UserSnapshotStore;
import com.lilamaris.cozyr.board.domain.UserSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateUserSnapshotService implements UpdateUserSnapshotUseCase {
    private final UserSnapshotStore store;

    @Override
    @Transactional
    public void update(UpdateUserSnapshotCommand command) {
        var userSnapshot = UserSnapshot.of(command.userId(), command.displayName(), command.updatedAt());
        store.upsert(userSnapshot);
    }
}
