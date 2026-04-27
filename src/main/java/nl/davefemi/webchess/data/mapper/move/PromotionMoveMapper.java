package nl.davefemi.webchess.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.move.PromotionMoveDTO;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.actions.PromotionMove;
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
