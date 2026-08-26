package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.port.in.command.CancelReserveCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.CancelReserveResult;

public interface CancelReserveUseCase {
    CancelReserveResult cancel(CancelReserveCommand command);
}
