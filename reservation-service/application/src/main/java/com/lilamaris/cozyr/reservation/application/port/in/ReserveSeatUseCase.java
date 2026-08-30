package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.port.in.command.ReserveSeatCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.ReserveSeatResult;

public interface ReserveSeatUseCase {
    ReserveSeatResult reserve(ReserveSeatCommand command);
}
