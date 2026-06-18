package nl.davefemi.chess.http.websocket.event;

import java.util.UUID;

public class RematchRequestEvent extends GameEvent{

    public RematchRequestEvent(UUID sessionId, String gameId, String requestedBy){
        super(sessionId, gameId, requestedBy);
    }
}
