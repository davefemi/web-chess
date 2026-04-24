package nl.davefemi.data.mapper;

import nl.davefemi.data.dto.GameResponseDTO;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GameResponseMapper {

    public GameResponseDTO mapToDTO(UUID gameId, @Nullable String color, Object message){
        GameResponseDTO dto = new GameResponseDTO();
        dto.setGameId(gameId.toString());
        dto.setColor(color);
        dto.setMessage(message);
        return dto;
    }
}
