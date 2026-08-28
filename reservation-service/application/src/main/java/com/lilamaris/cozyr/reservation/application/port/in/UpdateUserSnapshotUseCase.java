package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.port.in.command.UpdateUserSnapshotCommand;

public interface UpdateUserSnapshotUseCase {
    void update(UpdateUserSnapshotCommand command);
}
