package com.lilamaris.cozyr.identity.jpa;

import com.lilamaris.cozyr.identity.application.model.user.UserCursor;
import com.lilamaris.cozyr.identity.application.model.user.UserDetail;
import com.lilamaris.cozyr.identity.application.model.user.UserFilter;
import com.lilamaris.cozyr.identity.application.model.user.UserSummary;
import com.lilamaris.cozyr.identity.application.port.out.UserReader;
import com.lilamaris.cozyr.identity.application.port.out.UserStore;
import com.lilamaris.cozyr.identity.domain.User;
import com.lilamaris.cozyr.identity.jpa.repository.UserRepository;
import com.lilamaris.cozyr.identity.jpa.sql.UserSql;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserReader, UserStore {
    private final UserRepository repository;
    private final JdbcClient jdbcClient;

    @Override
    public boolean existsById(UUID userId) {
        return repository.existsById(userId);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<UserDetail> findDetailById(UUID userId) {
        return jdbcClient.sql(UserSql.FIND_DETAIL_BY_ID)
                .param("userId", userId)
                .query(UserDetail.class)
                .optional();
    }

    @Override
    public CursorResult<UserSummary, UserCursor> findSummaries(UserFilter filter, CursorRequest<UserCursor> request) {
        var sql = new StringBuilder(UserSql.LIST_SUMMARIES);
        var params = new MapSqlParameterSource();

        appendFilterCondition(sql, params, filter);
        appendCursorCondition(sql, params, request);

        var rows = jdbcClient.sql(sql.toString())
                .paramSource(params)
                .query(UserSummary.class)
                .list();

        boolean hasNext = rows.size() > request.size();

        var content = rows.stream().limit(request.size()).toList();

        UserCursor nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            nextCursor = Optional.ofNullable(content.getLast())
                    .map(last -> UserCursor.of(last.createdAt(), last.userId()))
                    .orElse(null);
        }

        return CursorResult.of(content, nextCursor, hasNext);
    }


    @Override
    public User save(User user) {
        return repository.save(user);
    }

    private void appendFilterCondition(
            StringBuilder sql,
            MapSqlParameterSource params,
            UserFilter filter
    ) {
        Optional.ofNullable(filter.displayName())
                .ifPresent(displayName -> {
                    sql.append("""
                            AND display_name ILIKE :displayName ESCAPE '\\'
                            """);
                    params.addValue("displayName", "%" + escapeLike(displayName) + "%");
                });
    }

    private void appendCursorCondition(
            StringBuilder sql,
            MapSqlParameterSource params,
            CursorRequest<UserCursor> request
    ) {
        Optional.ofNullable(request.cursor())
                .ifPresent(cursor -> {
                    sql.append("""
                            AND (
                                created_at <: cursorCreatedAt
                                OR (created_at = :cursorCreatedAt AND id < :cursorId) 
                            )
                            """);

                    params.addValue("cursorCreatedAt", cursor.createdAt());
                    params.addValue("cursorId", cursor.userId());
                });

        sql.append("""
                ORDER BY created_at DESC, id DESC
                LIMIT :limit
                """);

        params.addValue("limit", request.size() + 1);
    }

    private String escapeLike(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
