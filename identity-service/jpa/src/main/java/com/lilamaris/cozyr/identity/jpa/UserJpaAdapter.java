package com.lilamaris.cozyr.identity.jpa;

import com.lilamaris.cozyr.identity.application.model.UserDetail;
import com.lilamaris.cozyr.identity.application.port.out.UserReader;
import com.lilamaris.cozyr.identity.application.port.out.UserStore;
import com.lilamaris.cozyr.identity.domain.User;
import com.lilamaris.cozyr.identity.jpa.repository.UserRepository;
import com.lilamaris.cozyr.identity.jpa.sql.UserSql;
import lombok.RequiredArgsConstructor;
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
    public User save(User user) {
        return repository.save(user);
    }
}
