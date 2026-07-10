package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.DeletePostCommand;

public interface DeletePostUseCase {
    void delete(DeletePostCommand command);
}
