package nl.davefemi.chess.web.websocket.message;

import lombok.Data;
import java.util.Map;

@Data
public class GameMessage<T> {
    private String messageType;
    private String event;
    private Map<String,String> gameStatus;
    private T payload;
}
