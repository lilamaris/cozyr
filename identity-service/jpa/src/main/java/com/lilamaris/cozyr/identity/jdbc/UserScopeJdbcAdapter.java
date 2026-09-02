package com.lilamaris.cozyr.identity.jdbc;

import com.lilamaris.cozyr.identity.application.port.out.UserScopeStore;
import com.lilamaris.cozyr.identity.contract.schema.Scope;
import com.lilamaris.cozyr.identity.jdbc.sql.UserScopeSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserScopeJdbcAdapter implements UserScopeStore {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public boolean tryCreate(UUID userId, List<Scope> scopes, Instant createdAt) {
        var sql = UserScopeSql.INSERT_SCOPE;
        var args = scopes.stream()
                .map(scope -> new MapSqlParameterSource()
                        .addValue("userId", userId)
                        .addValue("service", scope.service())
                        .addValue("role", scope.role().getCanonicalName())
                        .addValue("createdAt", Timestamp.from(createdAt))
                )
                .toArray(SqlParameterSource[]::new);

        var result = jdbcTemplate.batchUpdate(sql, args);

        return result.length == scopes.size()
                && Arrays.stream(result).allMatch(e -> e == 1 || e == Statement.SUCCESS_NO_INFO);
    }
}
