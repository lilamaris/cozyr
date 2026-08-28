package com.lilamaris.cozyr.reservation.jdbc;

import com.lilamaris.cozyr.reservation.application.model.seat.SeatDetail;
import com.lilamaris.cozyr.reservation.application.model.seat.SeatSummary;
import com.lilamaris.cozyr.reservation.application.port.out.SeatDetailReader;
import com.lilamaris.cozyr.reservation.application.port.out.SeatSummaryReader;
import com.lilamaris.cozyr.reservation.domain.SeatId;
import com.lilamaris.cozyr.reservation.jdbc.row.SeatRow;
import com.lilamaris.cozyr.reservation.jdbc.sql.SeatSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SeatJdbcAdapter implements SeatSummaryReader, SeatDetailReader {
    private final JdbcClient jdbcClient;

    @Override
    public List<SeatSummary> find(long roomId) {
        var sql = SeatSql.LIST_SUMMARIES;

        var rows = jdbcClient.sql(sql)
                .param("roomId", roomId)
                .query(SeatRow.Summary.class)
                .list();

        return rows.stream()
                .filter(Objects::nonNull)
                .map(SeatRow.Summary::toSummary)
                .toList();
    }

    @Override
    public Optional<SeatDetail> findById(SeatId id) {
        var sql = SeatSql.FIND_DETAIL_BY_ID;

        return jdbcClient.sql(sql)
                .param("roomId", id.getRoomId())
                .param("seatId", id.getSeatId())
                .query(SeatRow.Detail.class)
                .optional()
                .map(SeatRow.Detail::toModel);
    }
}
