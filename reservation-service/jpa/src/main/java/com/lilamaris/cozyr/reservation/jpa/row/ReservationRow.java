package com.lilamaris.cozyr.reservation.jpa.row;

import com.lilamaris.cozyr.reservation.application.model.reservation.ReservationSummary;
import com.lilamaris.cozyr.reservation.application.model.room.RoomSchedule;
import com.lilamaris.cozyr.reservation.application.model.user.UserProjection;
import com.lilamaris.cozyr.reservation.domain.ReservationStatus;
import com.lilamaris.cozyr.reservation.domain.SeatId;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

public class ReservationRow {
    public record Summary(
            UUID reservationId,
            ReservationStatus status,
            Instant createdAt,
            Instant updatedAt,
            long roomId,
            String seatId,
            int occupiedSlotCount,
            UUID userId,
            String displayName
    ) {
        public ReservationSummary toSummary() {
            return ReservationSummary.of(
                    reservationId,
                    status,
                    SeatId.of(roomId, seatId),
                    createdAt,
                    updatedAt,
                    UserProjection.of(userId, displayName),
                    occupiedSlotCount
            );
        }
    }

    public record Detail(
            UUID reservationId,
            ReservationStatus status,
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
