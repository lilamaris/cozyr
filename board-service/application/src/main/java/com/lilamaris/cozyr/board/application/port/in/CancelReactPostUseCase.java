package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.CancelReactPostCommand;

public interface CancelReactPostUseCase {
    void cancel(CancelReactPostCommand command);
}
