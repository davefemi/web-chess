package nl.davefemi.data.mapper;

import nl.davefemi.data.dto.GameReponseDTO;
import nl.davefemi.domain.piece.PieceType;
import nl.davefemi.domain.piece.PlayerColor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GameResponseMapper {

    public GameReponseDTO mapToDTO(UUID gameId, @Nullable PlayerColor color, @Nullable PieceType type, Object message){
        GameReponseDTO dto = new GameReponseDTO();
        dto.setGameId(gameId.toString());
        dto.setColor(color == null? "": color.getColor());
        dto.setPiece(type == null?"" : type.getLabel());
        dto.setMessage(message);
        return dto;
    }
}
