package com.lilamaris.cozyr.reservation.jpa.row;

import com.lilamaris.cozyr.reservation.application.model.room.RoomDetail;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSummary;
import com.lilamaris.cozyr.reservation.application.model.schedule.LocalTimeSchedule;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class RoomRow {
    public record Summary(
            long roomId,
            String name,
            String description,
            Instant createdAt,
            Instant updatedAt
    ) {
        public RoomSummary toSummary() {
            return RoomSummary.of(roomId, name, description, createdAt, updatedAt);
        }
    }

    public record Detail(
            long roomId,
            String name,
            String description,
            Instant createdAt,
            Instant updatedAt,
            UUID slotId,
            LocalTime startAt,
            LocalTime endAt
    ) {
        public RoomDetail toDetail(List<LocalTimeSchedule> schedules) {
            return RoomDetail.of(roomId, name, description, createdAt, updatedAt, schedules);
        }

        public LocalTimeSchedule toSchedule() {
            return LocalTimeSchedule.of(startAt, endAt);
        }
    }
}
