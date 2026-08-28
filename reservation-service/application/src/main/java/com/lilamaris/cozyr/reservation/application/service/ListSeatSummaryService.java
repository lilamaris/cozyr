package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatSummary;
import com.lilamaris.cozyr.reservation.application.port.in.ListSeatSummaryUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.query.ListSeatSummaryQuery;
import com.lilamaris.cozyr.reservation.application.port.out.RoomReader;
import com.lilamaris.cozyr.reservation.application.port.out.SeatSummaryReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListSeatSummaryService implements ListSeatSummaryUseCase {
    private final SeatSummaryReader reader;

    private final RoomReader roomReader;

    @Override
    public List<SeatSummary> list(ListSeatSummaryQuery query) {
        var roomId = query.roomId();
        var roomExists = roomReader.existsById(roomId);
        if (!roomExists) throw new ApplicationException(ReservationServiceProgressCode.ROOM_NOT_FOUND);

        return reader.find(roomId);
    }
}
