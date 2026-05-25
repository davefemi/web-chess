package nl.davefemi.webchess.data.dto.record;

import lombok.Data;
import nl.davefemi.webchess.data.MoveRecordDTO;

@Data
public class PromotionMoveRecordDTO implements MoveRecordDTO {
    private String oldPos;
    private String newPos;
    private String playerColor;
    private int oldPieceId;
    private String newPieceType;
    private int newPieceId;
}
