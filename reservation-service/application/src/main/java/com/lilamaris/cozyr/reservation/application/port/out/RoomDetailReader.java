package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.room.RoomDetail;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.util.Optional;

public interface RoomDetailReader {
    Optional<RoomDetail> findById(RoomId roomId);
}
