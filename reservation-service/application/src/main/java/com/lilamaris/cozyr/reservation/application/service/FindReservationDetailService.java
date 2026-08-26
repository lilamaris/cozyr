package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationDetail;
import com.lilamaris.cozyr.reservation.application.port.in.FindReservationDetailUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindReservationDetailQuery;
import com.lilamaris.cozyr.reservation.application.port.out.ReservationDetailReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindReservationDetailService implements FindReservationDetailUseCase {
    private final ReservationDetailReader reader;

    @Override
    public ReservationDetail find(FindReservationDetailQuery query) {
        var reservationId = query.reservationId();

        return reader.find(reservationId)
                .orElseThrow(() -> new ApplicationException(ReservationServiceProgressCode.RESERVATION_NOT_FOUND));
    }
}
