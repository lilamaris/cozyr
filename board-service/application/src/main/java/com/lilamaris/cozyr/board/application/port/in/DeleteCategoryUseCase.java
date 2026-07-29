package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.DeleteCategoryCommand;

public interface DeleteCategoryUseCase {
    void delete(DeleteCategoryCommand command);
}
