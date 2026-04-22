package nl.davefemi.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.PromotionMoveDTO;
import nl.davefemi.domain.game.move.PromotionMove;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromotionMoveMapper {
    private final PositionMapper positionMapper;

    public PromotionMoveDTO mapDomainToDTO(PromotionMove move){
        PromotionMoveDTO dto = new PromotionMoveDTO();
        dto.setPosition(positionMapper.mapDomainToDTO(move.position()));
        dto.setPiece_type(move.newPiece());
        return dto;
    }

    public PromotionMove mapDTOtoDomain(PromotionMoveDTO move){
        return new PromotionMove(positionMapper.mapDTOtoDomain(move.getPosition()), move.getPiece_type());
    }
}
