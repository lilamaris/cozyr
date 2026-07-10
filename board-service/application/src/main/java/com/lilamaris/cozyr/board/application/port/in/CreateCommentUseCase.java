package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.CreateCommentCommand;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedCommentResult;

public interface CreateCommentUseCase {
    CreatedCommentResult create(CreateCommentCommand command);
}
