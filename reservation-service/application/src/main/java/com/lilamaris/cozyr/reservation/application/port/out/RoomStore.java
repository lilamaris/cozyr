package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.Room;

public interface RoomStore {
    Room save(Room room);
}
