package nl.davefemi.chess.web.websocket.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.data.mapper.session.SessionResponseMapper;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.SessionException;
import nl.davefemi.chess.exception.SessionNotFoundException;
import nl.davefemi.chess.web.dto.response.game.RematchAcceptanceResponse;
import nl.davefemi.chess.web.dto.response.game.RequestedRematchResponse;
import nl.davefemi.chess.web.websocket.message.GameMessageType;
import nl.davefemi.chess.web.websocket.service.GameMessagingService;
import nl.davefemi.chess.gameplay.service.GameQueryService;
import nl.davefemi.chess.session.model.GameSession;
import nl.davefemi.chess.session.model.PlayerPrincipal;
import nl.davefemi.chess.session.service.GameSessionService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameEventListener {
    private final GameMessagingService gameMessageService;
    private final GameQueryService gameQueryService;
    private final GameSessionService gameSessionService;
    private final SessionResponseMapper sessionResponseMapper;

    @EventListener
    public void onSubscribe(SessionSubscribeEvent event)
            throws SessionNotFoundException, BoardException, SessionException {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() == null || accessor.getDestination() == null)
            return;
        if (!accessor.getDestination().startsWith("/user"))
            return;
        PlayerPrincipal player = (PlayerPrincipal) accessor.getUser();
        gameMessageService.sendEventMessage(
                player.player().getSessionId(),
                player.player().getChannelId(),
                GameMessageType.GAME_STATE_UPDATED,
                EventType.PLAYER_SUBSCRIBED,
                gameQueryService.getCurrentGameState(player.player().getSessionId()));
        log.info("SessionId={}: new subscription", player.player().getSessionId());
    }

    //TODO: account for cases when session has expired
    @EventListener
    public void onCompletedMove(GameEvent<?> event){
        log(event);
        if (event.type() == EventType.MOVE_COMPLETED ||
                event.type() == EventType.PLAYER_SURRENDERED) {
            try {
                gameMessageService.sendEventMessage(
                        event.sessionId(),
                        gameSessionService.getGameSession(event.sessionId()).getOpponent(event.actionBy()).getChannelId(),
                        GameMessageType.GAME_STATE_UPDATED,
                        ((GameEvent<EventType>) event).type(),
                        gameQueryService.getCurrentGameState(event.sessionId()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @EventListener
    public void onRematchRequest(GameEvent<?> event) throws SessionNotFoundException, BoardException, SessionException {
        if(event.type() == EventType.REMATCH_REQUESTED) {
            log(event);
            GameSession session = gameSessionService.getGameSession(event.sessionId());
            RequestedRematchResponse response = sessionResponseMapper.getRequestedRematchResponse(
                    event.actionBy().getColor().toString());
            gameMessageService.sendEventMessage(
                    event.sessionId(),
                    session.getOpponent(event.actionBy()).getChannelId(),
                    GameMessageType.SESSION_UPDATE,
                    ((GameEvent<EventType>) event).type(),
                    response);
        }
    }

    @EventListener
    public void onAcceptedRematch(GameEvent<?> event) throws SessionNotFoundException, BoardException, SessionException {
        if (event.type() == EventType.REMATCH_ACCEPTED) {
            log(event);
            GameSession session = gameSessionService.getGameSession(event.sessionId());
            RematchAcceptanceResponse response = sessionResponseMapper.getRematchAcceptanceResponse(
                    true,
                    event.actionBy().getColor().toString());
            gameMessageService.sendEventMessage(
                    event.sessionId(),
                    session.getOpponent(event.actionBy()).getChannelId(),
                    GameMessageType.SESSION_UPDATE,
                    ((GameEvent<EventType>) event).type(),
                    response);
        }
    }

    @EventListener
    public void onDeclinedRematch(GameEvent<?> event) throws SessionNotFoundException, BoardException, SessionException {
        if (event.type() == EventType.REMATCH_DECLINED) {
            log(event);
            GameSession session = gameSessionService.getGameSession(event.sessionId());
            RematchAcceptanceResponse response = sessionResponseMapper.getRematchAcceptanceResponse(
                    false,
                    event.actionBy().getColor().toString());
            gameMessageService.sendEventMessage(
                    event.sessionId(),
                    session.getOpponent(event.actionBy()).getChannelId(),
                    GameMessageType.SESSION_UPDATE,
                    ((GameEvent<EventType>) event).type(),
                    response);
        }
    }

    private void log(GameEvent<?> event){
        log.info("SessionId={}: new event {} registered by {}", event.sessionId(), event.type(), event.actionBy().getColor());
    }
}
