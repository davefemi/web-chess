package nl.davefemi.chess.web.message.dto.move;

import lombok.Data;

@Data
public class PromotionMoveDto implements MoveDto {
    private String from;
    private String to;
    private String newPieceType;
}
