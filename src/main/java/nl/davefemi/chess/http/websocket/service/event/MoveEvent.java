package nl.davefemi.chess.http.websocket.service.event;

import java.util.UUID;
public class MoveEvent extends GameEvent{

    public MoveEvent(UUID sessionId, String gameId, String causedBy){
        super(sessionId, gameId, causedBy);
    }

}
