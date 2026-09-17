package com.lilamaris.cozyr.reservation.jdbc.row;

import com.lilamaris.cozyr.reservation.application.model.room.RoomDetail;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSummary;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class RoomRow {
    public record Summary(
            UUID roomId,
            String name,
            String description,
            Instant createdAt,
            Instant updatedAt
    ) {
        public RoomSummary toModel() {
            return RoomSummary.of(roomId, name, description, createdAt, updatedAt);
        }
    }

    public record Detail(
            UUID roomId,
            String name,
            String description,
            Instant createdAt,
            Instant updatedAt,
            UUID slotId,
            LocalTime startAt,
            LocalTime endAt
    ) {
        public RoomDetail toModel(List<RoomSchedule> schedules) {
            return RoomDetail.of(roomId, name, description, createdAt, updatedAt, schedules);
        }

        public RoomSchedule toSchedule() {
            return RoomSchedule.of(slotId, startAt, endAt);
        }
    }
}
