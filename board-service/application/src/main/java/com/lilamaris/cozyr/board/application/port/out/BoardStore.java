package com.lilamaris.cozyr.board.application.port.out;

import com.lilamaris.cozyr.board.domain.Board;

public interface BoardStore {
    Board save(Board board);
}