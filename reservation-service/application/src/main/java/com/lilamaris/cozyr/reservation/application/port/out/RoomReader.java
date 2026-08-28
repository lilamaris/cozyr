package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.Room;

import java.util.Optional;

public interface RoomReader {
    boolean existsById(long id);

    Optional<Room> findById(long id);
}
