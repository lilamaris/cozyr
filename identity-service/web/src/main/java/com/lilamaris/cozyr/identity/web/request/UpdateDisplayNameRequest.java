package com.lilamaris.cozyr.identity.web.request;

import com.lilamaris.cozyr.identity.application.port.in.command.UpdateDisplayNameCommand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "표시 이름 수정 요청")
public record UpdateDisplayNameRequest(
        @Schema(description = "수정할 표시 이름", example = "홍길동")
        String displayName
) {
    public UpdateDisplayNameCommand toCommand(UUID userId) {
        return UpdateDisplayNameCommand.of(userId, displayName);
    }
}
