package com.lilamaris.cozyr.reservation.application.port.in;

import com.lilamaris.cozyr.reservation.application.model.room.RoomDetail;
import com.lilamaris.cozyr.reservation.application.port.in.query.FindRoomDetailQuery;

public interface FindRoomDetailUseCase {
    RoomDetail find(FindRoomDetailQuery query);
}
