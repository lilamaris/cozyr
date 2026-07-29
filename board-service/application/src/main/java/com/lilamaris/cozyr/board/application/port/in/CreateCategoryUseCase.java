package com.lilamaris.cozyr.board.application.port.in;

import com.lilamaris.cozyr.board.application.port.in.command.CreateCategoryCommand;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedCategoryResult;

public interface CreateCategoryUseCase {
    CreatedCategoryResult create(CreateCategoryCommand command);
}
