package nl.davidfemi.data.mapper;

import nl.davidfemi.data.dto.GameReponseDTO;
import nl.davidfemi.domain.pieces.PieceType;
import nl.davidfemi.domain.pieces.PlayerColor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;
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
