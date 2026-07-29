package com.lilamaris.cozyr.board.persistence.jpa.repository;

import com.lilamaris.cozyr.board.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
