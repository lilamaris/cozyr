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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/boards/{boardId}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    private final IdentityContextHolder identityContextHolder;

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
