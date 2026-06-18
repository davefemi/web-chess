package nl.davefemi.chess.http.websocket.event;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.data.mapper.session.SessionResponseMapper;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.SessionException;
import nl.davefemi.chess.http.response.game.RematchAcceptanceResponse;
import nl.davefemi.chess.http.response.game.RequestedRematchResponse;
import nl.davefemi.chess.http.websocket.service.GameMessagingService;
import nl.davefemi.chess.play.service.GameQueryService;
import nl.davefemi.chess.session.model.GameSession;
import nl.davefemi.chess.session.model.PlayerPrincipal;
import nl.davefemi.chess.session.service.GameSessionService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import java.io.FileNotFoundException;

@Component
@RequiredArgsConstructor
public class GameEventListener {
    private final GameMessagingService gameMessageService;
    private final GameQueryService gameQueryService;
    private final GameSessionService gameSessionService;
    private final SessionResponseMapper sessionResponseMapper;

    @EventListener
    public void onSubscribe(SessionSubscribeEvent event)
            throws FileNotFoundException, SessionException, BoardException {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getUser() == null || accessor.getDestination() == null)
            return;
        if (!accessor.getDestination().startsWith("/topic/games"))
            return;
        PlayerPrincipal player = (PlayerPrincipal) accessor.getUser();
        gameMessageService.sendGameStateToPlayer(player.player().getMessageId(),
                gameQueryService.getCurrentGameState(player.player().getSessionId()));
    }

    @EventListener
    public void onMove(CompletedMoveEvent event){
        try {
            gameMessageService.publishGameState(
                    event.getGameId(),
                    gameQueryService.getCurrentGameState(event.getSessionId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @EventListener
    public void requestedRematch(RematchRequestEvent event) throws FileNotFoundException, SessionException, BoardException {
        GameSession session = gameSessionService.getGameSession(event.getSessionId());
        String player1MessageId = session.getPlayers().getFirst().getMessageId();
        String player2MessageId = session.getPlayers().getLast().getMessageId();
        RequestedRematchResponse response = sessionResponseMapper.getRequestedRematchResponse(
                event.getActionBy(),
                event.getGameId());
        gameMessageService.sendRequestedRematchResponse(player1MessageId, player2MessageId, response);
    }

    @EventListener
    public void acceptedRematch(RematchAcceptedEvent event) throws FileNotFoundException, SessionException, BoardException {
        GameSession session = gameSessionService.getGameSession(event.getSessionId());
        String player1MessageId = session.getPlayers().getFirst().getMessageId();
        String player2MessageId = session.getPlayers().getLast().getMessageId();
        RematchAcceptanceResponse response = sessionResponseMapper.getRematchAcceptanceResponse(
                true,
                event.getActionBy(),
                event.getGameId());
        gameMessageService.sendRematchAcceptanceResponse(player1MessageId,player2MessageId, response);
    }

    @EventListener
    public void declinedRematch(RematchDeclinedEvent event) throws FileNotFoundException, SessionException, BoardException {
        GameSession session = gameSessionService.getGameSession(event.getSessionId());
        String player1MessageId = session.getPlayers().getFirst().getMessageId();
        String player2MessageId = session.getPlayers().getLast().getMessageId();
        RematchAcceptanceResponse response = sessionResponseMapper.getRematchAcceptanceResponse(
                false,
                event.getActionBy(),
                event.getGameId());
        gameMessageService.sendRematchAcceptanceResponse(player1MessageId, player2MessageId, response);
    }
}
