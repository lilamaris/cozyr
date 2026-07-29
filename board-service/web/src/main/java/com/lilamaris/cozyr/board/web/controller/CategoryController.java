package com.lilamaris.cozyr.board.web.controller;

import com.lilamaris.cozyr.board.application.port.in.CreateCategoryUseCase;
import com.lilamaris.cozyr.board.application.port.in.DeleteCategoryUseCase;
import com.lilamaris.cozyr.board.application.port.in.UpdateCategoryUseCase;
import com.lilamaris.cozyr.board.application.port.in.command.DeleteCategoryCommand;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedCategoryResult;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedCategoryResult;
import com.lilamaris.cozyr.board.web.request.CreateCategoryRequest;
import com.lilamaris.cozyr.board.web.request.UpdateCategoryRequest;
import com.lilamaris.cozyr.identity.contract.context.IdentityContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/boards/{boardId}/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "카테고리 API")
public class CategoryController {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    private final IdentityContextHolder identityContextHolder;

    @Operation(summary = "카테고리 생성", description = "게시판에 새 카테고리를 생성합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid"))
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "카테고리 생성 성공",
                    content = @Content(schema = @Schema(implementation = CreatedCategoryResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "게시판을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping
    public ResponseEntity<CreatedCategoryResult> create(
            @PathVariable("boardId") UUID boardId,
            @Valid @RequestBody CreateCategoryRequest body
    ) {
        var identity = identityContextHolder.get();
        var command = body.toCommand(boardId, identity.id());
        var result = createCategoryUseCase.create(command);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.categoryId())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 이름과 설명을 수정합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "categoryId", description = "카테고리 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid"))
    })
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "카테고리 수정 성공",
                    content = @Content(schema = @Schema(implementation = UpdatedCategoryResult.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "카테고리를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @PostMapping("/{categoryId}")
    public ResponseEntity<UpdatedCategoryResult> update(
            @PathVariable("categoryId") UUID categoryId,
            @Valid @RequestBody UpdateCategoryRequest body
    ) {
        var identity = identityContextHolder.get();
        var command = body.toCommand(categoryId, identity.id());
        var result = updateCategoryUseCase.update(command);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid")),
            @Parameter(name = "categoryId", description = "카테고리 ID", required = true, in = ParameterIn.PATH, schema = @Schema(type = "string", format = "uuid"))
    })
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "카테고리 삭제 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "카테고리를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> delete(
            @PathVariable("categoryId") UUID categoryId
    ) {
        var identity = identityContextHolder.get();
        var command = DeleteCategoryCommand.of(categoryId, identity.id());
        deleteCategoryUseCase.delete(command);
        return ResponseEntity.noContent().build();
    }
}
