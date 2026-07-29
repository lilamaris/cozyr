package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.UpdateCategoryCommand;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedCategoryResult;

public interface UpdateCategoryUseCase {
    UpdatedCategoryResult update(UpdateCategoryCommand command);
}
