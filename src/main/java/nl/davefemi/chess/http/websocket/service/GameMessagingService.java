package nl.davefemi.chess.http.websocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.http.websocket.event.EventType;
import nl.davefemi.chess.http.websocket.message.GameMessage;
import nl.davefemi.chess.http.websocket.message.GameMessageType;
import nl.davefemi.chess.http.websocket.message.MessageMapper;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameMessagingService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageMapper messageMapper;
    private static final String PLAYER_ENDPOINT = "/user/%s/queue/games/updates";

    public <T> void sendEventMessage(String player, GameMessageType type, EventType event, T payload){
        GameMessage<T> message = messageMapper.mapToMessage(type, event, payload);
        messagingTemplate.convertAndSend(String.format(PLAYER_ENDPOINT, player), message);
        log.info("Executed playerEndpointId={}: sent message to {}", player, String.format(PLAYER_ENDPOINT, player));
    }

    public <T> void sendResponseMessage(String player, String correlationId, GameMessageType type, EventType event, T payload){
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
        headerAccessor.setHeader("correlation_id", correlationId);
        headerAccessor.setLeaveMutable(true);
        GameMessage<T> message = messageMapper.mapToMessage(type, event, payload);
        messagingTemplate.convertAndSend(String.format(PLAYER_ENDPOINT, player), message, headerAccessor.getMessageHeaders());
        log.info("Executed player={}: sent message to {}", player, String.format(PLAYER_ENDPOINT, player));
    }
}
