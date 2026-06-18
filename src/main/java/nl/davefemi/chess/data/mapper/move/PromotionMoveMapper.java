package nl.davefemi.chess.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.http.dto.move.PromotionMoveDto;
import nl.davefemi.chess.play.model.actions.move.PromotionMove;
import nl.davefemi.chess.play.model.actions.move.SingleMove;
import nl.davefemi.chess.play.model.board.PieceType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMoveMapper {
    private final  PositionMapper positionMapper;

    public PromotionMoveDto mapDomainToDTO(PromotionMove move){
        PromotionMoveDto dto = new PromotionMoveDto();
        dto.setTo(positionMapper.mapDomainToDTO(move.move().to()));
        dto.setFrom(positionMapper.mapDomainToDTO(move.move().from()));
        dto.setNewPieceType(move.newPieceType().toString());
        return dto;
    }

    public PromotionMove mapDTOtoDomain(PromotionMoveDto move){
        return new PromotionMove(new SingleMove(positionMapper.mapDTOtoDomain(move.getFrom()),
                positionMapper.mapDTOtoDomain(move.getTo())),
                PieceType.fromString(move.getNewPieceType()));
    }
}
