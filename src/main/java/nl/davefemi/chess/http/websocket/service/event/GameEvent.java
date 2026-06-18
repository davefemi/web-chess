package nl.davefemi.chess.http.websocket.service.event;

import lombok.Getter;

import java.util.UUID;

@Getter
abstract class GameEvent {
    private final UUID sessionId;
    private final String gameId;
    private final String causedBy;

    public GameEvent(UUID sessionId, String gameId, String causedBy){
        this.sessionId = sessionId;
        this.gameId = gameId;
        this.causedBy = causedBy;
    }
}
