package nl.davefemi.chess.http.websocket.service.event;

import java.util.UUID;

public class RematchDeclinedEvent extends GameEvent{
    public RematchDeclinedEvent(UUID sessionId, String causedBy) {
        super(sessionId, null, causedBy);
    }
}
