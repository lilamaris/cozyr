package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.domain.RoomId;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface RoomScheduleSlotReader {
    List<RoomSchedule> findAllByRoomId(RoomId roomId, Collection<UUID> scheduleSlotIds);
}
