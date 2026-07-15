package com.lilamaris.cozyr.identity.jpa;

import com.lilamaris.cozyr.identity.application.model.AuthenticatedPrincipal;
import com.lilamaris.cozyr.identity.application.port.out.PrincipalReader;
import com.lilamaris.cozyr.identity.jpa.row.PrincipalRow;
import com.lilamaris.cozyr.identity.jpa.sql.PrincipalSql;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PrincipalJpaAdapter implements PrincipalReader {
    private final JdbcClient jdbcClient;

    @Override
    public Optional<AuthenticatedPrincipal> findByUserId(UUID userId) {
        var rows = jdbcClient.sql(PrincipalSql.FIND_BY_USER_ID)
                .param("userId", userId)
                .query(PrincipalRow.class)
                .list();

        if (rows.isEmpty()) return Optional.empty();

        var first = rows.getFirst();
        if (first == null) return Optional.empty();

        var scopes = rows.stream()
                .filter(Objects::nonNull)
                .map(PrincipalRow::toScope)
                .flatMap(Optional::stream)
                .collect(Collectors.toUnmodifiableSet());

        var principal = AuthenticatedPrincipal.of(first.userId(), first.displayName(), scopes);
        return Optional.of(principal);
    }
}
