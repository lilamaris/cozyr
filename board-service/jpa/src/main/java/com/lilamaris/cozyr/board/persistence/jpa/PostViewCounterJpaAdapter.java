package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.port.out.PostViewCounter;
import com.lilamaris.cozyr.board.persistence.jpa.sql.PostViewSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.time.Instant;
import java.time.ZoneOffset;

@Component
@RequiredArgsConstructor
public class PostViewCounterJpaAdapter implements PostViewCounter {
    private final JdbcClient jdbcClient;

    @Override
    public void increase(long postId, Instant viewedAt) {
        jdbcClient.sql(PostViewSql.INCREASE)
                .param("postId", postId)
                .param("viewedAt", viewedAt.atOffset(ZoneOffset.UTC), Types.TIMESTAMP_WITH_TIMEZONE)
                .update();
    }
}
