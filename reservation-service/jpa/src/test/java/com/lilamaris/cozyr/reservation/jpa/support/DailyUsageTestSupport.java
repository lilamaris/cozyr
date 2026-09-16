package com.lilamaris.cozyr.reservation.jpa.support;

import org.assertj.core.api.AbstractOptionalAssert;
import org.assertj.core.api.ListAssert;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class DailyUsageTestSupport {
    private final static String FIND_USAGE_BY_USER_ID_ROOM_ID_RESERVATION_DATE = """
            SELECT
                id,
                user_id AS userId,
                room_id AS roomId,
                reservation_date AS reservationDate,
                reservation_count AS reservationCount
            FROM daily_reservation_usage
            WHERE user_id = :userId
                AND room_id = :roomId
                AND reservation_date = :reservationDate
            """;

    private final static String FIND_USAGE_BY_ROOM_ID_RESERVATION_DATE = """
            SELECT
                id,
                user_id AS userId,
                room_id AS roomId,
                reservation_date AS reservationDate,
                reservation_count AS reservationCount
            FROM daily_reservation_usage
            WHERE room_id = :roomId
                AND reservation_date = :reservationDate
            """;

    public static ListAssert<DailyReservationUsageAssertRow> assertDailyReservationUsageThat(JdbcClient jdbcClient, long roomId, LocalDate reservationDate) {
        var usages = jdbcClient.sql(FIND_USAGE_BY_ROOM_ID_RESERVATION_DATE)
                .param("roomId", roomId)
                .param("reservationDate", reservationDate)
                .query(DailyReservationUsageAssertRow.class)
                .list();

        return new ListAssert<>(usages);
    }

    public static DailyReservationUsageAssert assertDailyReservationUsageThat(JdbcClient jdbcClient, UUID userId, long roomId, LocalDate reservationDate) {
        var usages = jdbcClient.sql(FIND_USAGE_BY_USER_ID_ROOM_ID_RESERVATION_DATE)
                .param("userId", userId)
                .param("roomId", roomId)
                .param("reservationDate", reservationDate)
                .query(DailyReservationUsageAssertRow.class)
                .optional();

        return new DailyReservationUsageAssert(usages);
    }

    public record DailyReservationUsageAssertRow(UUID id, UUID userId, long roomId, LocalDate reservationDate,
                                                 int reservationCount) {
    }

    public static final class DailyReservationUsageAssert extends AbstractOptionalAssert<DailyReservationUsageAssert, DailyReservationUsageAssertRow> {
        DailyReservationUsageAssert(Optional<DailyReservationUsageAssertRow> actual) {
            super(actual, DailyReservationUsageAssert.class);
        }

        private DailyReservationUsageAssertRow extractOptional() {
            isPresent();

            return actual.orElse(null);
        }

        public DailyReservationUsageAssert hasRoomId(long roomId) {
            assertThat(extractOptional().roomId)
                    .isEqualTo(roomId);
            return this;
        }

        public DailyReservationUsageAssert hasCount(int count) {
            assertThat(extractOptional().reservationCount)
                    .isEqualTo(count);
            return this;
        }
    }
}
