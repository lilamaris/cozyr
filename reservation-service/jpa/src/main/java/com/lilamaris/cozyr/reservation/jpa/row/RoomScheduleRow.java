package com.lilamaris.cozyr.reservation.jpa.row;

import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;

import java.time.LocalTime;
import java.util.UUID;

public record RoomScheduleRow(
        UUID id,
        LocalTime startAt,
        LocalTime endAt
) {
    public RoomSchedule toSchedule() {
        return RoomSchedule.of(id, startAt, endAt);
    }
}
