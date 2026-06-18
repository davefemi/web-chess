package nl.davefemi.chess.data.dto.move;

import lombok.Data;
import nl.davefemi.chess.data.dto.MoveDTO;

@Data
public class PromotionMoveDTO implements MoveDTO {
    private String from;
    private String to;
    private String newPieceType;
}
