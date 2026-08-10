package com.lilamaris.cozyr.statistics.jpa;

import com.lilamaris.cozyr.statistics.application.port.out.DailyNewPostStore;
import com.lilamaris.cozyr.statistics.domain.DailyNewPost;
import com.lilamaris.cozyr.statistics.jpa.sql.DailyNewPostSql;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.time.ZoneOffset;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyNewPostStoreJpaAdapter implements DailyNewPostStore {
    private final JdbcClient jdbcClient;

    @Override
    public void upsert(DailyNewPost dailyNewPost) {
        var sql = DailyNewPostSql.UPSERT;

        var boardId = dailyNewPost.getId().getBoardId();
        var createdDate = dailyNewPost.getId().getCreatedDate();
        var createdCount = dailyNewPost.getCreatedCount();
        var createdAt = dailyNewPost.getCreatedAt().atOffset(ZoneOffset.UTC);
        var updatedAt = dailyNewPost.getUpdatedAt().atOffset(ZoneOffset.UTC);

        jdbcClient.sql(sql)
                .param("boardId", boardId)
                .param("createdDate", createdDate)
                .param("createdCount", createdCount)
                .param("createdAt", createdAt, Types.TIMESTAMP_WITH_TIMEZONE)
                .param("updatedAt", updatedAt, Types.TIMESTAMP_WITH_TIMEZONE)
                .update();
    }
}
