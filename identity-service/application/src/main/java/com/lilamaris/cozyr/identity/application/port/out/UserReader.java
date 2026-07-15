package com.lilamaris.cozyr.identity.application.port.out;

import com.lilamaris.cozyr.identity.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserReader {
    Optional<User> findById(UUID id);
}
