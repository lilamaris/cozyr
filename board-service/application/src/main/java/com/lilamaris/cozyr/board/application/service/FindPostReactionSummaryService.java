package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.exception.BoardServiceProgressCode;
import com.lilamaris.cozyr.board.application.model.reaction.PostReactionSummary;
import com.lilamaris.cozyr.board.application.port.in.FindPostReactionSummaryUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.FindPostReactionSummaryQuery;
import com.lilamaris.cozyr.board.application.port.out.PostReactionReader;
import com.lilamaris.cozyr.board.application.port.out.PostReader;
import com.lilamaris.shrturl.kernel.application.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindPostReactionSummaryService implements FindPostReactionSummaryUseCase {
    private final PostReactionReader reader;
    private final PostReader postReader;

    @Override
    public PostReactionSummary find(FindPostReactionSummaryQuery query) {
        var postId = query.postId();
        if (!postReader.existsById(postId))
            throw new ApplicationException(BoardServiceProgressCode.POST_NOT_FOUND);

        var filter = query.toFilter();

        return reader.findSummaries(filter)
                .orElseThrow(() -> new ApplicationException(BoardServiceProgressCode.REACTION_NOT_FOUND));
    }
}
