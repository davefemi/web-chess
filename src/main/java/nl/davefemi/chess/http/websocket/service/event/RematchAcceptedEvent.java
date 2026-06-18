package nl.davefemi.chess.http.websocket.service.event;

import java.util.UUID;

public class RematchAcceptedEvent extends GameEvent {


    public RematchAcceptedEvent(UUID sessionId, String gameId, String causedBy){
        super(sessionId, gameId, causedBy);
    }
}
