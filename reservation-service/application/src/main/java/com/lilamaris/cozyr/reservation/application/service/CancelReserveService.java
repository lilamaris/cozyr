package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.port.in.CancelReserveUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.command.CancelReserveCommand;
import com.lilamaris.cozyr.reservation.application.port.in.result.CancelReserveResult;
import com.lilamaris.cozyr.reservation.application.port.out.ReservationStore;
import com.lilamaris.cozyr.reservation.application.port.out.SeatOccupancyStore;
import com.lilamaris.cozyr.reservation.domain.ReservationStatus;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class CancelReserveService implements CancelReserveUseCase {
    private final ReservationStore store;
    private final SeatOccupancyStore seatOccupancyStore;
    private final Clock clock;

    @Override
    @Transactional
    public CancelReserveResult cancel(CancelReserveCommand command) {
        var reservationId = command.reservationId();
        var reservation = store.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(ReservationServiceProgressCode.RESERVATION_NOT_FOUND));

        if (reservation.getStatus() == ReservationStatus.CANCELED)
            throw new ApplicationException(ReservationServiceProgressCode.RESERVATION_ALREADY_CANCELED);

        var now = clock.instant();
        var isReleased = seatOccupancyStore.tryRelease(reservationId, now);
        if (!isReleased) throw new ApplicationException(ReservationServiceProgressCode.RESERVATION_ALREADY_CANCELED);

        reservation.cancel(now);

        return CancelReserveResult.of(reservationId, now);
    }
}
