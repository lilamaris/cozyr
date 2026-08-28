package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatDetail;
import com.lilamaris.cozyr.reservation.application.port.in.FindSeatDetailUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindSeatDetailQuery;
import com.lilamaris.cozyr.reservation.application.port.out.SeatDetailReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindSeatDetailService implements FindSeatDetailUseCase {
    private final SeatDetailReader reader;

    @Override
    public SeatDetail find(FindSeatDetailQuery query) {
        var id = query.id();
        return reader.findById(id)
                .orElseThrow(() -> new ApplicationException(ReservationServiceProgressCode.SEAT_NOT_FOUND));
    }
}
