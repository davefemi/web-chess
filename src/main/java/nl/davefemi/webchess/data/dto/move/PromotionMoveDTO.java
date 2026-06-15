package nl.davefemi.webchess.data.dto.move;

import lombok.Data;
import nl.davefemi.webchess.data.dto.MoveDTO;

@Data
public class PromotionMoveDTO implements MoveDTO {
    private String from;
    private String to;
    private String newPieceType;
}
