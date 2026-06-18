package nl.davefemi.chess.data.dto.record;

import lombok.Data;
import nl.davefemi.chess.data.dto.MoveRecordDTO;

@Data
public class PromotionMoveRecordDTO implements MoveRecordDTO {
    private String oldPos;
    private String newPos;
    private String playerColor;
    private String newPieceType;
    private int newPieceId;
    private String capturedPieceType;
    private int capturedPieceId;
}
