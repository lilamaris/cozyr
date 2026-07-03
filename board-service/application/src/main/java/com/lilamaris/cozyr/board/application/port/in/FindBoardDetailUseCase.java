package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.model.board.BoardDetail;
import com.lilamaris.cozyr.board.application.port.in.query.FindBoardDetailQuery;

public interface FindBoardDetailUseCase {
    BoardDetail find(FindBoardDetailQuery query);
}
