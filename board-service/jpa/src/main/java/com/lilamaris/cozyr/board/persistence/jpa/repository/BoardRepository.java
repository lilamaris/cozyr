package com.lilamaris.cozyr.board.persistence.jpa.repository;

import com.lilamaris.cozyr.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
}
