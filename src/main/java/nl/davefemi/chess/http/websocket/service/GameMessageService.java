package nl.davefemi.chess.http.websocket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.http.response.game.GameStateDto;
import nl.davefemi.chess.http.response.session.RematchAcceptanceResponse;
import nl.davefemi.chess.http.response.session.RequestedRematchResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameMessageService {
    private final SimpMessagingTemplate messagingTemplate;
    private static final String GAME_STATE_ENDPOINT = "/topic/games/%s/state";
    private static final String PLAYER_ENDPOINT = "/topic/games/players/%s";

    public void publishGameState(String id, GameStateDto payload){
        messagingTemplate.convertAndSend(String.format(GAME_STATE_ENDPOINT, id), payload);
        log.info("Executed gameId={}: sent message to {}", id, String.format(GAME_STATE_ENDPOINT, id));
    }

    public void sendMessage(String id, String message){
        messagingTemplate.convertAndSend(String.format(PLAYER_ENDPOINT, id), message);
        log.info("Executed playerId={}: sent message to {}", id, String.format(PLAYER_ENDPOINT, id));
    }

    public void sendGameStateToPlayer(String id, GameStateDto payload){
        messagingTemplate.convertAndSend(String.format(PLAYER_ENDPOINT, id), payload);
        log.info("Executed playerId={}: sent message to {}", id, String.format(PLAYER_ENDPOINT, id));
    }

    public void sendRequestedRematchResponse(String player1, String player2, RequestedRematchResponse response){
        messagingTemplate.convertAndSend(String.format(PLAYER_ENDPOINT, player1), response);
        messagingTemplate.convertAndSend(String.format(PLAYER_ENDPOINT, player2), response);
        log.info("Executed playerId={}, playerId={}: sent rematch invite message to {} and {}", player1, player2, String.format(PLAYER_ENDPOINT, player1),
                String.format(PLAYER_ENDPOINT, player2));
    }

    public void sendRematchAcceptanceResponse(String player1, String player2, RematchAcceptanceResponse response){
        messagingTemplate.convertAndSend(String.format(PLAYER_ENDPOINT, player1), response);
        messagingTemplate.convertAndSend(String.format(PLAYER_ENDPOINT, player2), response);
        log.info("Executed playerId={}, playerId={}: sent rematch acceptance message to {} and {}", player1, player2, String.format(PLAYER_ENDPOINT, player1),
                String.format(PLAYER_ENDPOINT, player2));
    }
}
