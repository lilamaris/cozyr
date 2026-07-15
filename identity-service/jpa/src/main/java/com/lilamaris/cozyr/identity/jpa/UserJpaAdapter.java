package com.lilamaris.cozyr.identity.jpa;

import com.lilamaris.cozyr.identity.application.port.out.UserReader;
import com.lilamaris.cozyr.identity.application.port.out.UserStore;
import com.lilamaris.cozyr.identity.domain.User;
import com.lilamaris.cozyr.identity.jpa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserReader, UserStore {
    private final UserRepository repository;

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }
}
