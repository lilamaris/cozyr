package com.lilamaris.cozyr.reservation.jpa.assertion;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import org.assertj.core.api.ListAssert;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class SeatOccupancyAssertion {

    private static final String FIND_OCCUPANCY_BY_RESERVATION_ID = """
            SELECT
                id,
                reservation_id AS reservationId,
                room_id AS roomId,
                seat_id AS seatId,
                occupancy_date AS occupancyDate,
                released_at AS releasedAt,
                schedule_slot_id AS slotId
            FROM seat_occupancy
            WHERE reservation_id = :reservationId
            """;

    private static final String FIND_ACTIVE_BY_SEAT_ID = """
            SELECT
                id,
                reservation_id AS reservationId,
                room_id AS roomId,
                seat_id AS seatId,
                occupancy_date AS occupancyDate,
                released_at AS releasedAt,
                schedule_slot_id AS slotId
            FROM seat_occupancy
            WHERE room_id = :roomId
                AND seat_id = :seatId
                AND occupancy_date = :occupancyDate
                AND released_at IS NULL
            """;

    public static ListAssert<SeatOccupancyAssertRow> assertSeatOccupancyThat(JdbcClient jdbcClient, UUID reservationId) {
        var occupancies = jdbcClient.sql(FIND_OCCUPANCY_BY_RESERVATION_ID)
                .param("reservationId", reservationId)
                .query(SeatOccupancyAssertRow.class)
                .list();

        return new ListAssert<>(occupancies);
    }

    public static ListAssert<SeatOccupancyAssertRow> assertActiveOccupanciesThat(JdbcClient jdbcClient, SeatLocator seatLocator, LocalDate date) {
        var occupancies = jdbcClient.sql(FIND_ACTIVE_BY_SEAT_ID)
                .param("roomId", seatLocator.roomId().getValue())
                .param("seatId", seatLocator.seatId().getValue())
                .param("occupancyDate", date)
                .query(SeatOccupancyAssertRow.class)
                .list();

        return new ListAssert<>(occupancies);
    }

    public record SeatOccupancyAssertRow(
            UUID id,
            UUID reservationId,
            UUID roomId,
            UUID seatId,
            LocalDate occupancyDate,
            Instant releasedAt,
            UUID slotId
    ) {
    }
}
