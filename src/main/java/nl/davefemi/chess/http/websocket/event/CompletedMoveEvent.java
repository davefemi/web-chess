package nl.davefemi.chess.http.websocket.event;

import java.util.UUID;
public class CompletedMoveEvent extends GameEvent{

    public CompletedMoveEvent(UUID sessionId, String gameId, String completedBy){
        super(sessionId, gameId, completedBy);
    }

}
