package com.lilamaris.cozyr.board.persistence.jpa;

import com.lilamaris.cozyr.board.application.model.post.PostCursor;
import com.lilamaris.cozyr.board.application.model.post.PostDetail;
import com.lilamaris.cozyr.board.application.model.post.PostFilter;
import com.lilamaris.cozyr.board.application.model.post.PostSummary;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.cozyr.board.domain.Post;
import com.lilamaris.cozyr.board.persistence.jpa.repository.PostRepository;
import com.lilamaris.cozyr.board.persistence.jpa.row.PostRow;
import com.lilamaris.cozyr.board.persistence.jpa.sql.PostSql;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostReaderJpaAdapter implements PostReader {
    private final PostRepository repository;
    private final JdbcClient jdbcClient;

    @Override
    public boolean existsById(long id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<Post> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<PostDetail> findDetailById(long id) {
        return jdbcClient.sql(PostSql.FIND_DETAIL_BY_ID)
                .param("id", id)
                .query(PostRow.Detail.class)
                .optional()
                .map(PostRow.Detail::toDetail);
    }

    @Override
    public CursorResult<PostSummary, PostCursor> findSummaries(UUID boardId, PostFilter filter, CursorRequest<PostCursor> request) {
        var conditions = new ArrayList<String>();
        var params = new MapSqlParameterSource()
                .addValue("boardId", boardId)
                .addValue("limit", request.size() + 1);

        appendFilterCondition(conditions, params, filter);
        appendCursorCondition(conditions, params, request.cursor());

        var dynamicWhere = conditions.isEmpty()
                ? ""
                : "AND " + String.join("\nAND ", conditions);

        var sql = PostSql.LIST_SUMMARIES.formatted(dynamicWhere);

        var rows = jdbcClient.sql(sql)
                    .paramSource(params)
                    .query(PostRow.Summary.class)
                    .list();

        boolean hasNext = rows.size() > request.size();

        var content = rows.stream()
                .limit(request.size())
                .filter(Objects::nonNull)
                .map(PostRow.Summary::toSummary)
                .toList();

        PostCursor nextCursor = null;
        if (hasNext && !content.isEmpty()) {
            nextCursor = Optional.ofNullable(content.getLast())
                    .map(last -> PostCursor.of(last.createdAt(), last.postId()))
                    .orElse(null);
        }

        return CursorResult.of(content, nextCursor, hasNext);
    }

    private void appendFilterCondition(
            List<String> conditions,
            MapSqlParameterSource params,
            PostFilter filter
    ) {
        Optional.ofNullable(filter.title()).ifPresent(title -> {
            conditions.add("p.title ILIKE :title");
            params.addValue("title", "%" + escapeLike(title) + "%");
        });

        Optional.ofNullable(filter.content()).ifPresent(content -> {
            conditions.add("p.content ILIKE :content");
            params.addValue("content", "%" + escapeLike(content) + "%");
        });

        Optional.ofNullable(filter.authorUserId()).ifPresent(authorUserId -> {
            conditions.add("p.author_user_id = :authorUserId");
            params.addValue("authorUserId", authorUserId);
        });

        Optional.ofNullable(filter.categoryId()).ifPresent(categoryId -> {
            conditions.add("p.category_id = :categoryId");
            params.addValue("categoryId", categoryId);
        });
    }

    private void appendCursorCondition(
            List<String> conditions,
            MapSqlParameterSource params,
            @Nullable PostCursor cursor
    ) {
        if (cursor == null) return;

        conditions.add("(p.creatd_at, p.id) < (:cursorCreatedAt, :cursorId)");
        params.addValue("cursorCreatedAt", cursor.createdAt());
        params.addValue("cursorId", cursor.postId());
    }

    private String escapeLike(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
