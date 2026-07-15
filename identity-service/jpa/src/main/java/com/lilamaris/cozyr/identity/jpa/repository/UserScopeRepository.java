package com.lilamaris.cozyr.identity.jpa.repository;

import com.lilamaris.cozyr.identity.domain.UserScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface UserScopeRepository extends JpaRepository<UserScope, UUID> {
    Set<UserScope> findByUserId(UUID userId);
}
