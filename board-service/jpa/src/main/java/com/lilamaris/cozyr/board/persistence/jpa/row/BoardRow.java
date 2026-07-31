package com.lilamaris.cozyr.board.persistence.jpa.row;

import com.lilamaris.cozyr.board.application.model.board.BoardDetail;
import com.lilamaris.cozyr.board.application.model.category.CategorySummary;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BoardRow {
    public record Detail(
            UUID boardId,
            String name,
            String description,
            Instant createdAt,
            Instant updatedAt,
            UUID categoryId,
            String categoryName
    ) {
        public Optional<CategorySummary> toCategory() {
            if (categoryId == null || categoryName == null) return Optional.empty();

            return Optional.of(CategorySummary.of(categoryId, categoryName));
        }

        public BoardDetail toDetail(List<CategorySummary> categories) {
            return BoardDetail.of(boardId, name, description, createdAt, updatedAt, categories);
        }
    }
}
