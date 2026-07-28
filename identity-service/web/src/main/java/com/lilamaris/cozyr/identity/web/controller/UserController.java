package com.lilamaris.cozyr.identity.web.controller;

import com.lilamaris.cozyr.identity.application.port.in.UpdateDisplayNameUseCase;
import com.lilamaris.cozyr.identity.application.port.in.result.UpdatedDisplayNameResult;
import com.lilamaris.cozyr.identity.contract.context.IdentityContextHolder;
import com.lilamaris.cozyr.identity.web.request.UpdateDisplayNameRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UpdateDisplayNameUseCase updateDisplayNameUseCase;
    private final IdentityContextHolder holder;

    @PatchMapping("/displayName")
    public ResponseEntity<UpdatedDisplayNameResult> updateDisplayName(
            @Valid @RequestBody UpdateDisplayNameRequest body
    ) {
        var identity = holder.get();

        var command = body.toCommand(identity.id());
        var result = updateDisplayNameUseCase.update(command);

        return ResponseEntity.ok(result);
    }
}
