package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.port.out.BoardStore;
import com.lilamaris.cozyr.board.domain.Board;
import com.lilamaris.cozyr.board.persistence.jpa.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BoardStoreJpaAdapter implements BoardStore {
    private final BoardRepository repository;

    @Override
    public Board save(Board board) {
        return repository.save(board);
    }
}
