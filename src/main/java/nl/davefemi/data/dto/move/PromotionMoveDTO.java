package nl.davefemi.data.dto.move;

import lombok.Data;

@Data
public class PromotionMoveDTO implements MoveDTO{
    private PositionDTO position;
    private String pieceType;
}
