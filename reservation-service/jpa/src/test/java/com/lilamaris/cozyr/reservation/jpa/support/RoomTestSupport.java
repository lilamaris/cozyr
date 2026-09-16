package com.lilamaris.cozyr.reservation.jpa.support;

import com.lilamaris.cozyr.reservation.domain.SeatId;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

public class RoomTestSupport {
    private static final long ROOM_ID = 1L;
    private static final LocalTime SLOT_START_AT = LocalTime.of(8, 0);
    private static final LocalTime SLOT_END_AT = LocalTime.of(9, 0);
    private static final int MAX_RESERVATION_PER_USER_PER_DAY = 3;
    private static final int MAX_SCHEDULE_PER_RESERVATION = 1;
    private static final String TARGET_SEAT_ID = "A1";
    private static final String OTHER_SEAT_ID = "B1";

    private static final String INSERT_ROOM = """
            INSERT INTO room (name, description, created_at)
            VALUES ('Test room', 'Test room description', :now)
            RETURNING id
            """;

    private static final String INSERT_SLOT = """
            INSERT INTO room_schedule_slot (room_id, start_at, end_at)
            VALUES (:roomId, :startAt, :endAt)
            RETURNING id
            """;

    private static final String INSERT_ROOM_OP = """
            INSERT INTO room_op_policy (room_id, max_reservation_per_user_per_day, max_schedule_per_reservation, updated_at)
            VALUES (:roomId, :maxReservationPerUserPerDay, :maxSchedulePerReservation, :now)
            RETURNING id
            """;

    private static final String INSERT_SEAT = """
            INSERT INTO seat (room_id, seat_id, created_at)
            VALUES (:roomId, :seatId, :now)
            RETURNING room_id AS roomId, seat_id AS seatId
            """;

    private static final String DELETE_ROOM = """
            DELETE FROM room WHERE id = :id
            """;

    private static final String DELETE_ROOM_OP = """
            DELETE FROM room_op_policy WHERE room_id = :roomId
            """;

    private static final String DELETE_SLOT = """
            DELETE FROM room_schedule_slot WHERE room_id = :roomId
            """;

    private static final String DELETE_SEAT = """
            DELETE FROM seat WHERE room_id = :roomId
            """;

    private static final String DELETE_SEAT_OCCUPANCY = """
            DELETE FROM seat_occupancy WHERE room_id = :roomId
            """;

    private static final String DELETE_RESERVATION = """
            DELETE FROM reservation WHERE room_id = :roomId
            """;

    private static final String DELETE_DAILY_RESERVATION_USAGE = """
            DELETE FROM daily_reservation_usage WHERE room_id = :roomId
            """;

    public static TestContext createContext(JdbcClient jdbcClient, Instant now) {
        var roomId = jdbcClient.sql(INSERT_ROOM)
                .param("now", Timestamp.from(now))
                .query(Long.class)
                .single();

        var slotId = jdbcClient.sql(INSERT_SLOT)
                .param("roomId", roomId)
                .param("startAt", SLOT_START_AT)
                .param("endAt", SLOT_END_AT)
                .query(UUID.class)
                .single();

        var roomOpId = jdbcClient.sql(INSERT_ROOM_OP)
                .param("roomId", roomId)
                .param("maxReservationPerUserPerDay", MAX_RESERVATION_PER_USER_PER_DAY)
                .param("maxSchedulePerReservation", MAX_SCHEDULE_PER_RESERVATION)
                .param("now", Timestamp.from(now))
                .query(UUID.class)
                .single();

        var targetSeatId = jdbcClient.sql(INSERT_SEAT)
                .param("roomId", roomId)
                .param("seatId", TARGET_SEAT_ID)
                .param("now", Timestamp.from(now))
                .query(SeatIdRow.class)
                .single();

        var secondSeatId = jdbcClient.sql(INSERT_SEAT)
                .param("roomId", roomId)
                .param("seatId", OTHER_SEAT_ID)
                .param("now", Timestamp.from(now))
                .query(SeatIdRow.class)
                .single();

        return new TestContext(roomId, slotId, roomOpId, targetSeatId.toId(), secondSeatId.toId());
    }

    public static void cleanup(JdbcClient jdbcClient, TestContext roomContext) {
        jdbcClient.sql(DELETE_ROOM)
                .param("id", roomContext.roomId)
                .update();

        jdbcClient.sql(DELETE_ROOM_OP)
                .param("roomId", roomContext.roomId)
                .update();

        jdbcClient.sql(DELETE_SLOT)
                .param("roomId", roomContext.roomId)
                .update();

        jdbcClient.sql(DELETE_SEAT)
                .param("roomId", roomContext.roomId)
                .update();

        jdbcClient.sql(DELETE_SEAT_OCCUPANCY)
                .param("roomId", roomContext.roomId)
                .update();

        jdbcClient.sql(DELETE_RESERVATION)
                .param("roomId", roomContext.roomId)
                .update();

        jdbcClient.sql(DELETE_DAILY_RESERVATION_USAGE)
                .param("roomId", roomContext.roomId)
                .update();
    }

    public record SeatIdRow(long roomId, String seatId) {
        public SeatId toId() {
            return SeatId.of(roomId, seatId);
        }
    }

    public record TestContext(long roomId, UUID slotId, UUID roomOpId, SeatId targetSeatId, SeatId otherSeatId) {
    }
}
