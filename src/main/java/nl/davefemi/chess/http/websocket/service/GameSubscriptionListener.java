package nl.davefemi.chess.http.websocket.service;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.SessionException;
import nl.davefemi.chess.play.service.GameQueryService;
import nl.davefemi.chess.session.model.PlayerPrincipal;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import java.io.FileNotFoundException;

@Component
@RequiredArgsConstructor
public class GameSubscriptionListener {
    private final GameMessageService gameMessageService;
    private final GameQueryService gameQueryService;

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
                gameQueryService.getGameState(player.player()));
    }
}
