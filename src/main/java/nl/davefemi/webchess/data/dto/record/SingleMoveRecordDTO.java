package nl.davefemi.webchess.data.dto.record;

import lombok.Data;
import nl.davefemi.webchess.data.MoveRecordDTO;

@Data
public class SingleMoveRecordDTO implements MoveRecordDTO {
    private String oldPos;
    private String newPos;
    private String playerColor;
    private String movedPieceType;
    private int movedPieceId;
    private String capturedPieceType;
    private int capturedPieceId;
}
