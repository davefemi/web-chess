package nl.davefemi.chess.http.websocket.event;

import lombok.Getter;

import java.util.UUID;

@Getter
abstract class GameEvent {
    private final UUID sessionId;
    private final String gameId;
    private final String actionBy;

    public GameEvent(UUID sessionId, String gameId, String actionBy){
        this.sessionId = sessionId;
        this.gameId = gameId;
        this.actionBy = actionBy;
    }
}
