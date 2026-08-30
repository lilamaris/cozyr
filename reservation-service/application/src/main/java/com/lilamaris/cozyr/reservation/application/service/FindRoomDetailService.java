package com.lilamaris.cozyr.reservation.application.service;

import com.lilamaris.cozyr.reservation.application.exception.ReservationServiceProgressCode;
import com.lilamaris.cozyr.reservation.application.model.room.RoomDetail;
import com.lilamaris.cozyr.reservation.application.port.in.FindRoomDetailUseCase;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindRoomDetailQuery;
import com.lilamaris.cozyr.reservation.application.port.out.RoomDetailReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindRoomDetailService implements FindRoomDetailUseCase {
    private final RoomDetailReader reader;

    @Override
    public RoomDetail find(FindRoomDetailQuery query) {
        var roomId = query.roomId();
        return reader.findById(roomId)
                .orElseThrow(() -> new ApplicationException(ReservationServiceProgressCode.ROOM_NOT_FOUND));
    }
}
