package nl.davefemi.webchess.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.move.CastlingMoveDTO;
import nl.davefemi.webchess.game.actions.move.CastlingMove;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CastlingMoveMapper {
    private final SingleMoveMapper moveMapper;
    private final PositionMapper positionMapper;

    public CastlingMove mapDTOtoDomain(CastlingMoveDTO dto){
        return new CastlingMove(
                new SingleMove(positionMapper.mapDTOtoDomain(dto.getKingFrom()),
                positionMapper.mapDTOtoDomain(dto.getKingTo())),
                new SingleMove(positionMapper.mapDTOtoDomain(dto.getRookFrom()),
                        positionMapper.mapDTOtoDomain(dto.getRookTo())));
    }

    public CastlingMoveDTO mapDomainToDTO(CastlingMove castlingMove){
        CastlingMoveDTO dto = new CastlingMoveDTO();
        dto.setKingFrom(positionMapper.mapDomainToDTO(castlingMove.moveKing().from()));
        dto.setKingTo(positionMapper.mapDomainToDTO(castlingMove.moveKing().to()));
        dto.setRookFrom(positionMapper.mapDomainToDTO(castlingMove.moveRook().from()));
        dto.setRookTo(positionMapper.mapDomainToDTO(castlingMove.moveRook().to()));
        return dto;
    }
}
