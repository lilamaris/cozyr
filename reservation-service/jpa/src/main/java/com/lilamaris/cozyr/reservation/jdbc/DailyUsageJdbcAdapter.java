package com.lilamaris.cozyr.reservation.jdbc;

import com.lilamaris.cozyr.reservation.application.port.out.DailyUsageCounter;
import com.lilamaris.cozyr.reservation.jdbc.sql.DailyUsageSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DailyUsageJdbcAdapter implements DailyUsageCounter {
    private final JdbcClient jdbcClient;

    @Override
    public boolean tryIncrease(UUID userId, Long roomId, LocalDate reservationDate, int maxCount) {
        var sql = DailyUsageSql.INCREASE_RESERVATION_COUNT;

        var updateCount = jdbcClient.sql(sql)
                .param("userId", userId)
                .param("roomId", roomId)
                .param("reservationDate", reservationDate)
                .param("maxCount", maxCount)
                .update();

        return updateCount > 0;
    }

    @Override
    public boolean tryDecrease(UUID userId, Long roomId, LocalDate reservationDate) {
        var sql = DailyUsageSql.DECREASE_RESERVATION_COUNT;

        var updateCount = jdbcClient.sql(sql)
                .param("userId", userId)
                .param("roomId", roomId)
                .param("reservationDate", reservationDate)
                .update();

        return updateCount > 0;
    }
}
