package com.lilamaris.cozyr.statistics.jpa;

import com.lilamaris.cozyr.statistics.application.port.out.DailyNewCommentStore;
import com.lilamaris.cozyr.statistics.domain.DailyNewComment;
import com.lilamaris.cozyr.statistics.jpa.sql.DailyNewCommentSql;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.time.ZoneOffset;

@Slf4j
@Component
@RequiredArgsConstructor
public class DailyNewCommentStoreJpaAdapter implements DailyNewCommentStore {
    private final JdbcClient jdbcClient;

    @Override
    public void upsert(DailyNewComment dailyNewComment) {
        var sql = DailyNewCommentSql.UPSERT;

        var postId = dailyNewComment.getId().getPostId();
        var createdDate = dailyNewComment.getId().getCreatedDate();
        var createdCount = dailyNewComment.getCreatedCount();
        var createdAt = dailyNewComment.getCreatedAt().atOffset(ZoneOffset.UTC);
        var updatedAt = dailyNewComment.getUpdatedAt().atOffset(ZoneOffset.UTC);

        jdbcClient.sql(sql)
                .param("postId", postId)
                .param("createdDate", createdDate)
                .param("createdCount", createdCount)
                .param("createdAt", createdAt, Types.TIMESTAMP_WITH_TIMEZONE)
                .param("updatedAt", updatedAt, Types.TIMESTAMP_WITH_TIMEZONE)
                .update();
    }
}
