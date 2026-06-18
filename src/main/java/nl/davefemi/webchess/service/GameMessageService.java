package nl.davefemi.webchess.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.dto.session.GameStateDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameMessageService {
    private final SimpMessagingTemplate messagingTemplate;
    private static final String GAME_STATE_ENDPOINT = "/topic/games/%s/state";
    private static final String PLAYER_ENDPOINT = "/topic/games/players/%s";

    public void sendGameState(String id, GameStateDTO payload){
        messagingTemplate.convertAndSend(String.format(GAME_STATE_ENDPOINT, id), payload);
        log.info("Executed gameId={}: sent message to {}", id, String.format(GAME_STATE_ENDPOINT, id));
    }

    public void sendMessage(String id, String message){
        messagingTemplate.convertAndSend(String.format(PLAYER_ENDPOINT, id), message);
        log.info("Executed playerId={}: sent message to {}", id, String.format(PLAYER_ENDPOINT, id));
    }
}
