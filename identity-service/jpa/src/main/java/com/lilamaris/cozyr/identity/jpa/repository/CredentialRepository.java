package com.lilamaris.cozyr.identity.jpa.repository;

import com.lilamaris.cozyr.identity.domain.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredentialRepository extends JpaRepository<Credential, UUID> {
    Optional<Credential> findByUserId(UUID userId);

    Optional<Credential> findByEmail(String email);

    boolean existsByEmail(String email);
}
