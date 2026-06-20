package nl.davefemi.chess.http.websocket.message;

import lombok.Data;
import nl.davefemi.chess.http.websocket.event.EventType;

@Data
public class GameMessage<T> {
    private GameMessageType type;
    private EventType event;
    private T message;
}
