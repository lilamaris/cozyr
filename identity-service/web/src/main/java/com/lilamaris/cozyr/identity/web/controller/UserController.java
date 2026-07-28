package com.lilamaris.cozyr.identity.web.controller;

import com.lilamaris.cozyr.identity.application.model.user.UserCursor;
import com.lilamaris.cozyr.identity.application.model.user.UserDetail;
import com.lilamaris.cozyr.identity.application.model.user.UserFilter;
import com.lilamaris.cozyr.identity.application.model.user.UserSummary;
import com.lilamaris.cozyr.identity.application.port.in.FindUserDetailUseCase;
import com.lilamaris.cozyr.identity.application.port.in.ListUserSummaryUseCase;
import com.lilamaris.cozyr.identity.application.port.in.UpdateDisplayNameUseCase;
import com.lilamaris.cozyr.identity.application.port.in.query.FindUserDetailQuery;
import com.lilamaris.cozyr.identity.application.port.in.query.ListUserSummaryQuery;
import com.lilamaris.cozyr.identity.application.port.in.result.UpdatedDisplayNameResult;
import com.lilamaris.cozyr.identity.contract.context.IdentityContextHolder;
import com.lilamaris.cozyr.identity.web.request.UpdateDisplayNameRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UpdateDisplayNameUseCase updateDisplayNameUseCase;
    private final ListUserSummaryUseCase listUserSummaryUseCase;
    private final FindUserDetailUseCase findUserDetailUseCase;

    private final IdentityContextHolder holder;

    @GetMapping
    public ResponseEntity<CursorResult<UserSummary, UserCursor>> list(
            @RequestParam(name = "displayName", required = false) String displayName,
            @RequestParam(name = "uid", required = false) UUID userId,
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @RequestParam(name = "size") int size
    ) {
        UserCursor cursor = null;
        if (createdAt != null || userId != null) {
            cursor = UserCursor.of(createdAt, userId);
        }

        var filter = UserFilter.empty()
                .withDisplayName(displayName);

        var query = ListUserSummaryQuery.of(filter, cursor, size);
        var result = listUserSummaryUseCase.list(query);
        return ResponseEntity.ok(result);
    }

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
