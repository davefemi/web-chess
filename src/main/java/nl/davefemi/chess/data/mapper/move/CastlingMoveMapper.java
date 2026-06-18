package nl.davefemi.chess.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.http.dto.move.CastlingMoveDto;
import nl.davefemi.chess.play.model.actions.move.CastlingMove;
import nl.davefemi.chess.play.model.actions.move.SingleMove;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CastlingMoveMapper {
    private final PositionMapper positionMapper;

    public CastlingMove mapDTOtoDomain(CastlingMoveDto dto){
        return new CastlingMove(
                new SingleMove(positionMapper.mapDTOtoDomain(dto.getKingFrom()),
                positionMapper.mapDTOtoDomain(dto.getKingTo())),
                new SingleMove(positionMapper.mapDTOtoDomain(dto.getRookFrom()),
                        positionMapper.mapDTOtoDomain(dto.getRookTo())));
    }

    public CastlingMoveDto mapDomainToDTO(CastlingMove castlingMove){
        CastlingMoveDto dto = new CastlingMoveDto();
        dto.setKingFrom(positionMapper.mapDomainToDTO(castlingMove.moveKing().from()));
        dto.setKingTo(positionMapper.mapDomainToDTO(castlingMove.moveKing().to()));
        dto.setRookFrom(positionMapper.mapDomainToDTO(castlingMove.moveRook().from()));
        dto.setRookTo(positionMapper.mapDomainToDTO(castlingMove.moveRook().to()));
        return dto;
    }
}
