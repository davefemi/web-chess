package nl.davefemi.chess.http.websocket.service.event;

import java.util.UUID;

public class RematchRequestEvent extends GameEvent{

    public RematchRequestEvent(UUID sessionId, String gameId, String causedBy){
        super(sessionId, gameId, causedBy);
    }
}
