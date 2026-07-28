package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.port.out.UserSnapshotStore;
import com.lilamaris.cozyr.board.domain.UserSnapshot;
import com.lilamaris.cozyr.board.persistence.jpa.sql.UserSnapshotSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserSnapshotStoreJpaAdapter implements UserSnapshotStore {
    private final JdbcClient jdbcClient;

    @Override
    public UserSnapshot upsert(UserSnapshot userSnapshot) {
        return jdbcClient.sql(UserSnapshotSql.UPSERT)
                .param("userId", userSnapshot.getUserId())
                .param("displayName", userSnapshot.getDisplayName())
                .param("lastUpdatedAt", userSnapshot.getLastUpdatedAt().atOffset(ZoneOffset.UTC), Types.TIMESTAMP_WITH_TIMEZONE)
                .query((rs, rowNum) -> {
                    var userId = rs.getObject("user_id", UUID.class);
                    var displayName = rs.getString("display_name");
                    var lastUpdatedAt = rs.getObject("last_updated_at", OffsetDateTime.class).toInstant();
                    return UserSnapshot.of(userId, displayName, lastUpdatedAt);
                })
                .single();
    }
}
