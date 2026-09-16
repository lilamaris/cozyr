package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.room.RoomContext;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.util.Optional;

public interface RoomContextReader {
    Optional<RoomContext> findByRoomId(RoomId roomId);
}
