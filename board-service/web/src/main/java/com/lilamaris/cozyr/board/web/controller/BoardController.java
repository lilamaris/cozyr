package com.lilamaris.cozyr.board.web.controller;

import com.lilamaris.cozyr.board.application.model.board.BoardCursor;
import com.lilamaris.cozyr.board.application.model.board.BoardDetail;
import com.lilamaris.cozyr.board.application.model.board.BoardFilter;
import com.lilamaris.cozyr.board.application.model.cursor.CursorResult;
import com.lilamaris.cozyr.board.application.port.in.CreateBoardUseCase;
import com.lilamaris.cozyr.board.application.port.in.FindBoardUseCase;
import com.lilamaris.cozyr.board.application.port.in.ListBoardUseCase;
import com.lilamaris.cozyr.board.application.port.in.UpdateBoardUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.FindBoardQuery;
import com.lilamaris.cozyr.board.application.port.in.query.ListBoardQuery;
import com.lilamaris.cozyr.board.application.port.in.result.CreatedBoardResult;
import com.lilamaris.cozyr.board.application.port.in.result.UpdatedBoardResult;
import com.lilamaris.cozyr.board.web.request.CreateBoardRequest;
import com.lilamaris.cozyr.board.web.request.UpdateBoardRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class BoardController {
    private final CreateBoardUseCase createBoardUseCase;
    private final UpdateBoardUseCase updateBoardUseCase;

    private final FindBoardUseCase findBoardUseCase;
    private final ListBoardUseCase listBoardUseCase;

    @PostMapping
    public ResponseEntity<CreatedBoardResult> create(
            @Valid @RequestBody CreateBoardRequest body
    ) {
        var command = body.toCommand();
        var result = createBoardUseCase.create(command);

        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{boardId}")
                .buildAndExpand(result.id())
                .toUri();
        return ResponseEntity.created(location).body(result);
    }

    @PutMapping("/{boardId}")
    public ResponseEntity<UpdatedBoardResult> update(
            @PathVariable("boardId") UUID boardId,
            @Valid @RequestBody UpdateBoardRequest body
    ) {
        var command = body.toCommand(boardId);
        var result = updateBoardUseCase.update(command);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<CursorResult<BoardDetail, BoardCursor>> list(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "bid", required = false) UUID boardId,
            @RequestParam(name = "ca", required = false) Instant createdAt,
            @RequestParam(name = "size") int size
    ) {
        BoardCursor cursor = null;
        if (createdAt != null && boardId != null) {
            cursor = BoardCursor.of(createdAt, boardId);
        }

        var filter = BoardFilter.empty()
                .withName(name)
                .withDescription(description);

        var query = ListBoardQuery.of(filter, cursor, size);
        var result = listBoardUseCase.list(query);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDetail> find(
            @PathVariable("boardId") UUID boardId
    ) {
        var query = FindBoardQuery.of(boardId);
        var result = findBoardUseCase.find(query);
        return ResponseEntity.ok(result);
    }
}
