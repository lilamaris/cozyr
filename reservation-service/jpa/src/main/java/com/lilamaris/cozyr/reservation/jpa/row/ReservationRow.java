package com.lilamaris.cozyr.reservation.jpa.row;

import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.application.model.user.UserProjection;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

public class ReservationRow {
    public record Detail(
            UUID reservationId,
            Instant createdAt,
            Instant updatedAt,
            long roomId,
            String seatId,
            UUID slotId,
            LocalTime startAt,
            LocalTime endAt,
            UUID userId,
            String displayName
    ) {
        public SeatId toSeatId() {
            return SeatId.of(roomId, seatId);
        }

        public RoomSchedule toRoomSchedule() {
            return RoomSchedule.of(slotId, startAt, endAt);
        }

        public UserProjection toUserProjection() {
            return UserProjection.of(userId, displayName);
        }
    }
}
