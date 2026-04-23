package nl.davefemi.data.dto.move;

import lombok.Data;
import nl.davefemi.data.dto.PositionDTO;

@Data
public class PromotionMoveDTO implements MoveDTO{
    private PositionDTO position;
    private String pieceType;
}
