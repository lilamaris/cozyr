package com.lilamaris.cozyr.reservation.application.port.out;

import java.util.Collection;
import java.util.UUID;

public interface RoomScheduleSlotReader {
    boolean existsByRoom(long roomId, Collection<UUID> scheduleSlotIds);
}
