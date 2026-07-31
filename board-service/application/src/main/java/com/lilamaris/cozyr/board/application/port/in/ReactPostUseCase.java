package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.ReactPostCommand;
import com.lilamaris.cozyr.board.application.port.in.result.ReactedPostResult;

public interface ReactPostUseCase {
    ReactedPostResult react(ReactPostCommand command);
}
