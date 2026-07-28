package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.UpdateUserSnapshotCommand;

public interface UpdateUserSnapshotUseCase {
    void update(UpdateUserSnapshotCommand command);
}
