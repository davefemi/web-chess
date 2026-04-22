package nl.davefemi.data.dto;

import lombok.Data;

@Data
public class PromotionMoveDTO implements MoveDTO{
    private PositionDTO position;
    private String piece_type;
}
