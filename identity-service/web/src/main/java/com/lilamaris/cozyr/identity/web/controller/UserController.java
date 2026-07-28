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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "사용자 API")
public class UserController {
    private final UpdateDisplayNameUseCase updateDisplayNameUseCase;
    private final ListUserSummaryUseCase listUserSummaryUseCase;
    private final FindUserDetailUseCase findUserDetailUseCase;

    private final IdentityContextHolder holder;

    @Operation(summary = "사용자 목록 조회", description = "사용자를 커서 기반으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = CursorResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping
    public ResponseEntity<CursorResult<UserSummary, UserCursor>> list(
            @Parameter(description = "표시 이름 검색어", schema = @Schema(type = "string", example = "홍길동"))
            @RequestParam(name = "displayName", required = false) String displayName,
            @Parameter(description = "커서 사용자 ID", schema = @Schema(type = "string", format = "uuid"))
            @RequestParam(name = "uid", required = false) UUID userId,
            @Parameter(description = "커서 생성 시각", schema = @Schema(type = "string", format = "date-time"))
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @Parameter(description = "조회 개수", required = true, schema = @Schema(type = "integer", minimum = "1", example = "20"))
            @RequestParam(name = "size") int size
    ) {
        UserCursor cursor = null;
        if (createdAt != null && userId != null) {
            cursor = UserCursor.of(createdAt, userId);
        }

        var filter = UserFilter.empty()
                .withDisplayName(displayName);

        var query = ListUserSummaryQuery.of(filter, cursor, size);
        var result = listUserSummaryUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "내 정보 조회", description = "현재 인증된 사용자의 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "내 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserDetail.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/me")
    public ResponseEntity<UserDetail> findMe() {
        var identity = holder.get();

        var query = FindUserDetailQuery.of(identity.id());
        var result = findUserDetailUseCase.find(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "사용자 상세 조회", description = "사용자 ID로 사용자 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserDetail.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetail> findDetail(
            @Parameter(
                    description = "사용자 ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable("userId") UUID userId
    ) {
        var query = FindUserDetailQuery.of(userId);
        var result = findUserDetailUseCase.find(query);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "표시 이름 수정", description = "현재 인증된 사용자의 표시 이름을 수정합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "표시 이름 수정 성공",
                    content = @Content(schema = @Schema(implementation = UpdatedDisplayNameResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
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
