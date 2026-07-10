package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.UpdateCommentCommand;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedCommentResult;

public interface UpdateCommentUseCase {
    UpdatedCommentResult update(UpdateCommentCommand command);
}
