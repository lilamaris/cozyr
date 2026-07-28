package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.CreateUserSnapshotCommand;

public interface CreateUserSnapshotUseCase {
    void create(CreateUserSnapshotCommand command);
}
