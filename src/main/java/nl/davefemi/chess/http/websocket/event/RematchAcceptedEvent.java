package nl.davefemi.chess.http.websocket.event;

import java.util.UUID;

public class RematchAcceptedEvent extends GameEvent {
    public RematchAcceptedEvent(UUID sessionId, String gameId, String acceptedBy){
        super(sessionId, gameId, acceptedBy);
    }
}
