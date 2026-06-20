package nl.davefemi.chess.http.websocket.message;

import nl.davefemi.chess.http.websocket.event.EventType;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public <T> GameMessage<T> mapToMessage(GameMessageType type, EventType event, T payload){
        GameMessage<T> message = new GameMessage<>();
        message.setType(type);
        message.setEvent(event);
        message.setMessage(payload);
        return message;
    }
}
