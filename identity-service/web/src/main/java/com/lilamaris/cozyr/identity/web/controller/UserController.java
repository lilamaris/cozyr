package com.lilamaris.cozyr.identity.web.controller;

import com.lilamaris.cozyr.identity.application.model.UserDetail;
import com.lilamaris.cozyr.identity.application.port.in.FindUserDetailUseCase;
import com.lilamaris.cozyr.identity.application.port.in.UpdateDisplayNameUseCase;
import com.lilamaris.cozyr.identity.application.port.in.query.FindUserDetailQuery;
import com.lilamaris.cozyr.identity.application.port.in.result.UpdatedDisplayNameResult;
import com.lilamaris.cozyr.identity.contract.context.IdentityContextHolder;
import com.lilamaris.cozyr.identity.web.request.UpdateDisplayNameRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final FindUserDetailUseCase findUserDetailUseCase;
    private final UpdateDisplayNameUseCase updateDisplayNameUseCase;
    private final IdentityContextHolder holder;

    @GetMapping("/me")
    public ResponseEntity<UserDetail> findMe() {
        var identity = holder.get();

        var query = FindUserDetailQuery.of(identity.id());
        var result = findUserDetailUseCase.find(query);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDetail> findDetail(
            @PathVariable("userId") UUID userId
    ) {
        var query = FindUserDetailQuery.of(userId);
        var result = findUserDetailUseCase.find(query);
        return ResponseEntity.ok(result);
    }

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
