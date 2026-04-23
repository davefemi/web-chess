package nl.davefemi.data.mapper;

import nl.davefemi.data.dto.GameReponseDTO;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GameResponseMapper {

    public GameReponseDTO mapToDTO(UUID gameId, @Nullable String color, Object message){
        GameReponseDTO dto = new GameReponseDTO();
        dto.setGameId(gameId.toString());
        dto.setColor(color);
        dto.setMessage(message);
        return dto;
    }
}
