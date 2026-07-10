package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.DeleteCommentCommand;

public interface DeleteCommentUseCase {
    void delete(DeleteCommentCommand command);
}
