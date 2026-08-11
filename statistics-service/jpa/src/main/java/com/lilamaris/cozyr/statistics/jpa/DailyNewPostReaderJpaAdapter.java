package com.lilamaris.cozyr.statistics.jpa;

import com.lilamaris.cozyr.statistics.application.model.point.DailyPoint;
import com.lilamaris.cozyr.statistics.application.model.post.DailyNewPostStatistics;
import com.lilamaris.cozyr.statistics.application.port.out.DailyNewPostReader;
import com.lilamaris.cozyr.statistics.jpa.row.DailyPointRow;
import com.lilamaris.cozyr.statistics.jpa.sql.DailyNewPostSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DailyNewPostReaderJpaAdapter implements DailyNewPostReader {
    private final JdbcClient jdbcClient;

    @Override
    public Optional<DailyNewPostStatistics> findBoardStatistics(UUID boardId, LocalDate from, LocalDate to) {
        var sql = DailyNewPostSql.FIND_STATISTICS_BY_BOARD_ID;

        var rows = jdbcClient.sql(sql)
                .param("boardId", boardId)
                .param("from", from)
                .param("to", to)
                .query(DailyPointRow.class)
                .list();

        if (rows.isEmpty()) return Optional.empty();

        var points = rows.stream()
                .filter(Objects::nonNull)
                .map(DailyPointRow::toPoint)
                .toList();

        var totalCount = points.stream()
                .mapToLong(DailyPoint::count)
                .sum();

        return Optional.of(
                DailyNewPostStatistics.of(boardId, from, to, totalCount, points)
        );
    }
}
