package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.port.in.command.CreateRoomCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.RoomCreatedResult;

public interface CreateRoomUseCase {
    RoomCreatedResult create(CreateRoomCommand command);
}
