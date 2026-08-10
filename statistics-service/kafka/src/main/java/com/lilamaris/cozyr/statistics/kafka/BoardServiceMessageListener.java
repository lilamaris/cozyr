package com.lilamaris.cozyr.statistics.kafka;

import com.lilamaris.cozyr.board.contract.event.CommentCreatedEvent;
import com.lilamaris.cozyr.board.contract.event.PostCreatedEvent;
import com.lilamaris.cozyr.kernel.message.MessageEnvelope;
import com.lilamaris.cozyr.statistics.application.port.in.AggregateDailyCommentCreationUseCase;
import com.lilamaris.cozyr.statistics.application.port.in.AggregateDailyPostCreationUseCase;
import com.lilamaris.cozyr.statistics.application.port.in.command.AggregateDailyCommentCreatedCommand;
import com.lilamaris.cozyr.statistics.application.port.in.command.AggregateDailyPostCreatedCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.ZoneOffset;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardServiceMessageListener {
    private final AggregateDailyPostCreationUseCase aggregateDailyPostCreationUseCase;
    private final AggregateDailyCommentCreationUseCase aggregateDailyCommentCreationUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "post.created",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void handlerPostCreated(MessageEnvelope<?> message, Acknowledgment ack) {
        log.info("Handle Post Created Message: {}", message);

        var event = objectMapper.convertValue(message.payload(), PostCreatedEvent.class);
        var createdDate = event.createdAt().atOffset(ZoneOffset.UTC).toLocalDate();

        var command = AggregateDailyPostCreatedCommand.of(event.boardId(), createdDate, 1L);
        aggregateDailyPostCreationUseCase.aggregate(command);
    }

    @KafkaListener(
            topics = "comment.created",
            containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    public void handleCommentCreated(MessageEnvelope<?> message, Acknowledgment ack) {
        log.info("Handle Comment Created Message: {}", message);

        var event = objectMapper.convertValue(message.payload(), CommentCreatedEvent.class);
        var createdDate = event.createdAt().atOffset(ZoneOffset.UTC).toLocalDate();

        var command = AggregateDailyCommentCreatedCommand.of(event.postId(), createdDate, 1L);
        aggregateDailyCommentCreationUseCase.aggregate(command);
    }
}
