package nl.davefemi.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.move.PromotionMoveDTO;
import nl.davefemi.data.dto.move.SingleMoveDTO;
import nl.davefemi.game.actions.SingleMove;
import nl.davefemi.game.board.PieceType;
import nl.davefemi.game.actions.PromotionMove;
import nl.davefemi.game.rule.MoveEvaluator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMoveMapper {
    private final SingleMoveMapper singleMoveMapper;

    public PromotionMoveDTO mapDomainToDTO(PromotionMove move){
        PromotionMoveDTO dto = new PromotionMoveDTO();
        dto.setMove(singleMoveMapper.mapDomainToDTO(move.move()));
        dto.setPieceType(move.newPieceType().getLabel());
        return dto;
    }

    public PromotionMove mapDTOtoDomain(PromotionMoveDTO move){
        return new PromotionMove(singleMoveMapper.mapDTOtoDomain(move.getMove()), PieceType.fromString(move.getPieceType()));
    }
}
