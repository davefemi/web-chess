package nl.davefemi.chess.data.dto.record;

import lombok.Data;
import nl.davefemi.chess.data.dto.MoveRecordDTO;

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
