package nl.davefemi.chess.web.websocket.message;

import nl.davefemi.chess.web.websocket.event.EventType;
import nl.davefemi.chess.gameplay.model.game.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public <T> GameMessage<T> mapToMessage(GameMessageType type, EventType event, GameStatus status, T payload){
        GameMessage<T> message = new GameMessage<>();
        message.setMessageType(type.toString());
        message.setEvent(event.toString());
        message.setGameStatus(status.getStatus());
        message.setPayload(payload);
        return message;
    }

    public <T> GameMessage<T> mapToErrorMessage(GameMessageType type, GameStatus status, T payload){
        GameMessage<T> message = new GameMessage<>();
        message.setMessageType(type.toString());
        message.setEvent(null);
        message.setGameStatus(status.getStatus());
        message.setPayload(payload);
        return message;
    }

}
