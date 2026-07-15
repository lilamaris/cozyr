package com.lilamaris.cozyr.identity.jpa.repository;

import com.lilamaris.cozyr.identity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
