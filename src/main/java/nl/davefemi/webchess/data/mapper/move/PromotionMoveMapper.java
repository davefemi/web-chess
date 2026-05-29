package nl.davefemi.webchess.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.move.PromotionMoveDTO;
import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.PieceType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMoveMapper {
    private final SingleMoveMapper singleMoveMapper;
    private final  PositionMapper positionMapper;

    public PromotionMoveDTO mapDomainToDTO(PromotionMove move){
        PromotionMoveDTO dto = new PromotionMoveDTO();

        dto.setTo(positionMapper.mapDomainToDTO(move.move().to()));
        dto.setFrom(positionMapper.mapDomainToDTO(move.move().from()));
        dto.setNewPieceType(move.newPieceType().getLabel());
        return dto;
    }

    public PromotionMove mapDTOtoDomain(PromotionMoveDTO move){
        return new PromotionMove(new SingleMove(positionMapper.mapDTOtoDomain(move.getFrom()),
                positionMapper.mapDTOtoDomain(move.getTo())),
                PieceType.fromString(move.getNewPieceType()));
    }
}
