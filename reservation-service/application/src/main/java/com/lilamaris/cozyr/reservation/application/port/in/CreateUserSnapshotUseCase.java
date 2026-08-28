package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.port.in.command.CreateUserSnapshotCommand;

public interface CreateUserSnapshotUseCase {
    void create(CreateUserSnapshotCommand command);
}
