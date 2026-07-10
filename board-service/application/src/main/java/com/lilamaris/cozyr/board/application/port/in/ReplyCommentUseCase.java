package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.ReplyCommentCommand;
import com.lilamaris.cozyr.board.application.port.in.result.RepliedCommentResult;

public interface ReplyCommentUseCase {
    RepliedCommentResult reply(ReplyCommentCommand command);
}
