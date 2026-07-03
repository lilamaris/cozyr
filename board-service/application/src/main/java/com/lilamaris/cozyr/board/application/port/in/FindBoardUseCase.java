package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.board.BoardDetail;
import com.lilamaris.cozyr.board.application.port.in.query.FindBoardQuery;

public interface FindBoardUseCase {
    BoardDetail find(FindBoardQuery query);
}
