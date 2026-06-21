package nl.davefemi.chess.web.websocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.SessionException;
import nl.davefemi.chess.exception.SessionNotFoundException;
import nl.davefemi.chess.web.dictionary.StompEndpoints;
import nl.davefemi.chess.web.dictionary.StompHeaders;
import nl.davefemi.chess.web.websocket.event.EventType;
import nl.davefemi.chess.web.websocket.message.GameMessage;
import nl.davefemi.chess.web.websocket.message.GameMessageType;
import nl.davefemi.chess.web.websocket.message.MessageMapper;
import nl.davefemi.chess.gameplay.model.game.GameStatus;
import nl.davefemi.chess.gameplay.service.GameQueryService;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class GameMessagingService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;
    private final GameQueryService gameQueryService;

    public <T> void sendEventMessage(UUID sessionId, String channelId, GameMessageType type, EventType event,  T payload) throws SessionException, BoardException, SessionNotFoundException {
        GameStatus gameStatus = gameQueryService.getCurrentGameStatus(sessionId);
        GameMessage<T> message = messageMapper.mapToMessage(type, event, gameStatus, payload);
        messagingTemplate.convertAndSend(String.format(StompEndpoints.PLAYER_ENDPOINT, channelId), message);
        log(channelId);
    }

    private <T> void sendResponseMessage
            (UUID sessionId, String channelId, String correlationId, EventType event, T response) throws SessionException, BoardException, SessionNotFoundException {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        headerAccessor.setNativeHeader(StompHeaders.CORRELATION_ID, correlationId);
        headerAccessor.setLeaveMutable(true);
        GameStatus gameStatus = gameQueryService.getCurrentGameStatus(sessionId);
        GameMessage<T> message = messageMapper.mapToMessage(GameMessageType.COMMAND_RESPONSE, event, gameStatus, response);
        messagingTemplate.convertAndSend(String.format(StompEndpoints.PLAYER_ENDPOINT, channelId), message, headerAccessor.getMessageHeaders());
        log(channelId);
    }

    public <T> void sendErrorResponse(UUID sessionId, String channelId, String correlationId, T response)
            throws SessionException, BoardException, SessionNotFoundException {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        headerAccessor.setNativeHeader(StompHeaders.CORRELATION_ID, correlationId);
        headerAccessor.setLeaveMutable(true);
        GameStatus gameStatus = gameQueryService.getCurrentGameStatus(sessionId);
        GameMessage<T> message = messageMapper.mapToErrorMessage(GameMessageType.ERROR, gameStatus, response);
        messagingTemplate.convertAndSend(String.format(StompEndpoints.PLAYER_ENDPOINT, channelId), message, headerAccessor.getMessageHeaders());
    }

    public <T> void sendAcceptedMoveResponse(UUID sessionId, String channelId, String correlationId, T response) throws SessionException, BoardException, SessionNotFoundException {
        sendResponseMessage(
                sessionId,
                channelId,
                correlationId,
                EventType.MOVE_ACCEPTED,
                response);
    }

    public <T> void sendSurrenderResponse(UUID sessionId, String channelId, String correlationId, T response) throws SessionException, BoardException, SessionNotFoundException {
        sendResponseMessage(
                sessionId,
                channelId,
                correlationId,
                EventType.PLAYER_SURRENDERED,
                response);
    }

    public <T> void sendRequestedRematchResponse(UUID sessionId, String channelId, String correlationId, T response) throws SessionException, BoardException, SessionNotFoundException {
        sendResponseMessage(
                sessionId,
                channelId,
                correlationId,
                EventType.REMATCH_REQUESTED,
                response);
    }

    public <T> void sendAcceptedRematchResponse(UUID sessionId, String channelId, String correlationId, T response) throws SessionException, BoardException, SessionNotFoundException {
        sendResponseMessage(
                sessionId,
                channelId,
                correlationId,
                EventType.REMATCH_ACCEPTED,
                response);
    }

    public <T> void sendDeclinedRematchResponse(UUID sessionId, String channelId, String correlationId, T response) throws SessionException, BoardException, SessionNotFoundException {
        sendResponseMessage(
                sessionId,
                channelId,
                correlationId,
                EventType.REMATCH_DECLINED,
                response);
    }

    public <T> void sendEndSessionResponse(UUID sessionId, String channelId, String correlationId, T response) throws SessionException, BoardException, SessionNotFoundException {
        sendResponseMessage(
                sessionId,
                channelId,
                correlationId,
                EventType.SESSION_ENDED,
                response);
    }

    private void log(String channelId){
        log.info("Executed channelId={}: sent message to {}", channelId, String.format(StompEndpoints.PLAYER_ENDPOINT, channelId));
    }
}
