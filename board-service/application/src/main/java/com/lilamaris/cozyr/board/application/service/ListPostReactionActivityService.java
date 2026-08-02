package com.lilamaris.cozyr.board.application.service;

import com.lilamaris.cozyr.board.application.model.reaction.PostReactionActivity;
import com.lilamaris.cozyr.board.application.port.in.ListPostReactionActivityUseCase;
import com.lilamaris.cozyr.board.application.port.in.query.ListPostReactionActivityQuery;
import com.lilamaris.cozyr.board.application.port.out.PostReactionReader;
import com.lilamaris.shrturl.kernel.application.model.cursor.CursorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListPostReactionActivityService implements ListPostReactionActivityUseCase {
    private final PostReactionReader reader;

    @Override
    public List<PostReactionActivity> list(ListPostReactionActivityQuery query) {
        var request = CursorRequest.of(query.cursor(), query.size());
        return reader.findActivities(query.userId(), request);
    }
}
