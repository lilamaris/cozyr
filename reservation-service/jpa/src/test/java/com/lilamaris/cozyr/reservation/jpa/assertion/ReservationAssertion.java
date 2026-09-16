package com.lilamaris.cozyr.reservation.jpa.assertion;

import com.lilamaris.cozyr.reservation.domain.ReservationStatus;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import org.assertj.core.api.AbstractOptionalAssert;
import org.assertj.core.api.ListAssert;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationAssertion {
    private static final String FIND_RESERVATION_BY_ID = """
            SELECT
                id,
                reserved_user_id AS reservedUserId,
                room_id AS roomId,
                seat_id AS seatId,
                occupancy_date AS occupancyDate,
                status,
                created_at AS createdAt,
                updated_at AS updatedAt
            FROM reservation
            WHERE id = :id
            """;

    private static final String FIND_RESERVATIONS_BY_ROOM_ID = """
            SELECT
                id,
                reserved_user_id AS reservedUserId,
                room_id AS roomId,
                seat_id AS seatId,
                occupancy_date AS occupancyDate,
                status,
                created_at AS createdAt,
                updated_at AS updatedAt
            FROM reservation
            WHERE room_id = :roomId
            """;

    public static ListAssert<ReservationAssertRow> assertReservationByRoomThat(JdbcClient jdbcClient, long roomId) {
        var reservations = jdbcClient.sql(FIND_RESERVATIONS_BY_ROOM_ID)
                .param("roomId", roomId)
                .query(ReservationAssertRow.class)
                .list();

        return new ListAssert<>(reservations);
    }

    public static ReservationAssert assertReservationThat(JdbcClient jdbcClient, UUID id) {
        var reservation = jdbcClient.sql(FIND_RESERVATION_BY_ID)
                .param("id", id)
                .query(ReservationAssertRow.class)
                .optional();

        return new ReservationAssert(reservation);
    }

    public record ReservationAssertRow(UUID id, UUID reservedUserId, long roomId, String seatId,
                                       LocalDate occupancyDate, ReservationStatus status, Instant createdAt,
                                       Instant updatedAt) {
    }

    public static final class ReservationAssert extends AbstractOptionalAssert<ReservationAssert, ReservationAssertRow> {
        ReservationAssert(Optional<ReservationAssertRow> reservation) {
            super(reservation, ReservationAssert.class);
        }

        private ReservationAssertRow extractOptional() {
            isPresent();

            return actual.orElse(null);
        }

        public ReservationAssert hasRoomId(long roomId) {
            assertThat(extractOptional().roomId).isEqualTo(roomId);
            return this;
        }

        public ReservationAssert hasSeatId(String seatId) {
            assertThat(extractOptional().seatId).isEqualTo(seatId);
            return this;
        }

        public ReservationAssert hasSeatId(SeatId seatId) {
            assertThat(extractOptional())
                    .as("reservation.[roomId, seatId]")
                    .extracting(
                            ReservationAssertRow::roomId,
                            ReservationAssertRow::seatId
                    )
                    .containsExactly(seatId.getRoomId(), seatId.getSeatId());
            return this;
        }

        public ReservationAssert hasStatus(ReservationStatus status) {
            assertThat(extractOptional().status)
                    .as("reservation.status")
                    .isEqualTo(status);
            return this;
        }

        public ReservationAssert hasOccupancyDate(LocalDate date) {
            assertThat(extractOptional().occupancyDate)
                    .as("reservation.occupancyDate")
                    .isEqualTo(date);
            return this;
        }

        public ReservationAssert hasReservedUserId(UUID userId) {
            assertThat(extractOptional().reservedUserId)
                    .as("reservation.reservedUserId")
                    .isEqualByComparingTo(userId);
            return this;
        }
    }
}
