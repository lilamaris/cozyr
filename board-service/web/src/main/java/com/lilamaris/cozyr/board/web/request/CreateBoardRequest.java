package com.lilamaris.cozyr.board.web.request;

import com.lilamaris.cozyr.board.application.port.in.command.CreateBoardCommand;

public record CreateBoardRequest(
        String name,
        String description
) {
    public CreateBoardCommand toCommand() {
        return CreateBoardCommand.of(name, description);
    }
}
