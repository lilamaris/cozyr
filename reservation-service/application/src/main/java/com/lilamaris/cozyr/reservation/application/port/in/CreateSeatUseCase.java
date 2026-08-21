package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.port.in.command.CreateSeatCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.SeatCreatedResult;

public interface CreateSeatUseCase {
    SeatCreatedResult create(CreateSeatCommand command);
}
