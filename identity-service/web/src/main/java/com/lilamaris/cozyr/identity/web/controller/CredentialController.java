package com.lilamaris.cozyr.identity.web.controller;

import com.lilamaris.cozyr.identity.application.port.in.UpdatePasswordUseCase;
import com.lilamaris.cozyr.identity.contract.context.IdentityContextHolder;
import com.lilamaris.cozyr.identity.web.request.UpdatePasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/credential")
@RequiredArgsConstructor
public class CredentialController {
    private final UpdatePasswordUseCase updatePasswordUseCase;

    private final IdentityContextHolder identityContextHolder;

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest body
    ) {
        var identity = identityContextHolder.get();
        var command = body.toCommand(identity.id());
        updatePasswordUseCase.update(command);
        return ResponseEntity.noContent().build();
    }
}
