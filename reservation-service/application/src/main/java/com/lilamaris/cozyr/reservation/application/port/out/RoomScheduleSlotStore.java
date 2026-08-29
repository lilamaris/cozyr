package com.lilamaris.cozyr.reservation.application.port.out;

import com.lilamaris.cozyr.reservation.domain.RoomScheduleSlot;

import java.util.List;

public interface RoomScheduleSlotStore {
    List<RoomScheduleSlot> saveAll(Iterable<RoomScheduleSlot> slots);
}
