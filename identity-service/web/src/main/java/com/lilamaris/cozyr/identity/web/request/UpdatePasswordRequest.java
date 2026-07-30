package com.lilamaris.cozyr.identity.web.request;

import com.lilamaris.cozyr.identity.application.port.in.command.UpdatePasswordCommand;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "비밀번호 변경 요청")
public record UpdatePasswordRequest(
        @Schema(description = "현재 비밀번호", example = "old-password-123!")
        String originalPassword,
        @Schema(description = "새 비밀번호", example = "new-password-123!")
        String newPassword
) {
    public UpdatePasswordCommand toCommand(UUID userId) {
        return UpdatePasswordCommand.of(userId, originalPassword, newPassword);
    }
}
