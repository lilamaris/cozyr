package com.lilamaris.cozyr.statistics.jpa;

import com.lilamaris.cozyr.statistics.application.model.comment.DailyNewCommentStatistics;
import com.lilamaris.cozyr.statistics.application.model.point.DailyPoint;
import com.lilamaris.cozyr.statistics.application.port.out.DailyNewCommentReader;
import com.lilamaris.cozyr.statistics.jpa.row.DailyPointRow;
import com.lilamaris.cozyr.statistics.jpa.sql.DailyNewCommentSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DailyNewCommentReaderJpaAdapter implements DailyNewCommentReader {
    private final JdbcClient jdbcClient;

    @Override
    public Optional<DailyNewCommentStatistics> findPostStatistics(long postId, LocalDate from, LocalDate to) {
        var sql = DailyNewCommentSql.FIND_STATISTICS_BY_POST_ID;

        var rows = jdbcClient.sql(sql)
                .param("postId", postId)
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
                DailyNewCommentStatistics.of(from, to, totalCount, points)
        );
    }
}
