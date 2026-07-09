package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.CreatePostCommand;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedPostResult;

public interface CreatePostUseCase {
    CreatedPostResult create(CreatePostCommand command);
}
