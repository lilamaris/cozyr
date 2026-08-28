package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.port.in.command.UpdateRoomCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.RoomUpdatedResult;

public interface UpdateRoomUseCase {
    RoomUpdatedResult update(UpdateRoomCommand command);
}
