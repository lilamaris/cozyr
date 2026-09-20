package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.port.in.command.ActivateRoomCommand;

public interface ActivateRoomUseCase {
    void activate(ActivateRoomCommand command);
}
