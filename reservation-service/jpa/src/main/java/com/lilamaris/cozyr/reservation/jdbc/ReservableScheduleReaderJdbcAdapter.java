package com.lilamaris.cozyr.reservation.jdbc;

import com.lilamaris.cozyr.reservation.application.model.seat.ReservableSeatSchedule;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatLocator;
import com.lilamaris.cozyr.reservation.application.port.out.ReservableScheduleReader;
import com.lilamaris.cozyr.reservation.jdbc.row.RoomScheduleRow;
import com.lilamaris.cozyr.reservation.jdbc.sql.ReservableScheduleSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ReservableScheduleReaderJdbcAdapter implements ReservableScheduleReader {
    private final JdbcClient jdbcClient;
    private final Clock clock;

    @Override
    public ReservableSeatSchedule findBySeat(LocalDate targetDate, SeatLocator seatLocator) {
        var sql = ReservableScheduleSql.FIND_BY_SEAT;
        var now = clock.instant();

        var rows = jdbcClient.sql(sql)
                .param("roomId", seatLocator.roomId().getValue())
                .param("seatId", seatLocator.seatId().getValue())
                .param("occupancyDate", targetDate)
                .param("now", Timestamp.from(now))
                .query(RoomScheduleRow.class)
                .list();

        var schedules = rows.stream()
                .filter(Objects::nonNull)
                .map(RoomScheduleRow::toSchedule)
                .toList();

        return ReservableSeatSchedule.of(
                targetDate,
                seatLocator.roomId(),
                seatLocator.seatId(),
                schedules
        );
    }
}
