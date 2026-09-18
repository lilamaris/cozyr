package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.port.in.command.DeactivateRoomCommand;

public interface DeactivateRoomUseCase {
    void deactivate(DeactivateRoomCommand command);
}
