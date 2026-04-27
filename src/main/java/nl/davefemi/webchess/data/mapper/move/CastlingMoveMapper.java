package nl.davefemi.webchess.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.move.CastlingMoveDTO;
import nl.davefemi.webchess.game.actions.CastlingMove;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CastlingMoveMapper {
    private final SingleMoveMapper moveMapper;

    public CastlingMove mapDTOtoDomain(CastlingMoveDTO dto){
        return new CastlingMove(moveMapper.mapDTOtoDomain(dto.getKingMove()),
                moveMapper.mapDTOtoDomain(dto.getRookMove()));
    }

    public CastlingMoveDTO mapDomainToDTO(CastlingMove castlingMove){
        CastlingMoveDTO dto = new CastlingMoveDTO();
        dto.setKingMove(moveMapper.mapDomainToDTO(castlingMove.moveKing()));
        dto.setRookMove(moveMapper.mapDomainToDTO(castlingMove.moveRook()));
        return dto;
    }
}
