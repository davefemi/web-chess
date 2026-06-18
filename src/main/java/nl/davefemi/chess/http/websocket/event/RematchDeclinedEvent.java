package nl.davefemi.chess.http.websocket.event;

import java.util.UUID;

public class RematchDeclinedEvent extends GameEvent{
    public RematchDeclinedEvent(UUID sessionId, String declinedBy) {
        super(sessionId, null, declinedBy);
    }
}
