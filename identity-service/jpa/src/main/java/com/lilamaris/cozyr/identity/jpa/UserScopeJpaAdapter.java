package com.lilamaris.cozyr.identity.jpa;

import com.lilamaris.cozyr.identity.application.port.out.UserScopeReader;
import com.lilamaris.cozyr.identity.domain.UserScope;
import com.lilamaris.cozyr.identity.jpa.repository.UserScopeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserScopeJpaAdapter implements UserScopeReader {
    private final UserScopeRepository repository;

    @Override
    public Optional<UserScope> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Set<UserScope> findByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }
}
