package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.Room;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.util.Optional;

public interface RoomReader {
    boolean existsById(RoomId roomId);

    Optional<Room> findById(RoomId roomId);
}
