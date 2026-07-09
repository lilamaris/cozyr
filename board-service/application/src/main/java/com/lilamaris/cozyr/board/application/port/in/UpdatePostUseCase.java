package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.UpdatePostCommand;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedPostResult;

public interface UpdatePostUseCase {
    UpdatedPostResult update(UpdatePostCommand command);
}
