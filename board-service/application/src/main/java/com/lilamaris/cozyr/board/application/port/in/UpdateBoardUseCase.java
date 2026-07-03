package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.UpdateBoardCommand;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedBoardResult;

public interface UpdateBoardUseCase {
    UpdatedBoardResult update(UpdateBoardCommand command);
}
