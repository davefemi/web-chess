package nl.davefemi.data.mapper.move;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.move.PromotionMoveDTO;
import nl.davefemi.data.mapper.PositionMapper;
import nl.davefemi.domain.game.actions.move.PromotionMove;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMoveMapper {
    private final PositionMapper positionMapper;

    public PromotionMoveDTO mapDomainToDTO(PromotionMove move){
        PromotionMoveDTO dto = new PromotionMoveDTO();
        dto.setPosition(positionMapper.mapDomainToDTO(move.position()));
        dto.setPieceType(move.newPiece());
        return dto;
    }

    public PromotionMove mapDTOtoDomain(PromotionMoveDTO move){
        return new PromotionMove(positionMapper.mapDTOtoDomain(move.getPosition()), move.getPieceType());
    }
}
