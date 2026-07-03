package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.CreateBoardCommand;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedBoardResult;

public interface CreateBoardUseCase {
    CreatedBoardResult create(CreateBoardCommand command);
}
