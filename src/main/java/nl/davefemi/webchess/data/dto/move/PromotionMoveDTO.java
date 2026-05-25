package nl.davefemi.webchess.data.dto.move;

import lombok.Data;
import nl.davefemi.webchess.data.MoveDTO;

@Data
public class PromotionMoveDTO implements MoveDTO {
    private SingleMoveDTO move;
    private String pieceType;
}
