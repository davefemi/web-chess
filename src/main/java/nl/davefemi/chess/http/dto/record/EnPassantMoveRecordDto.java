package nl.davefemi.chess.http.dto.record;

import lombok.Data;
import nl.davefemi.chess.http.dto.MoveRecordDto;

@Data
public class EnPassantMoveRecordDto implements MoveRecordDto {
    private String oldPos;
    private String newPos;
    private String playerColor;
    private String movedPieceType;
    private int movedPieceId;
    private String capturedPieceType;
    private int capturedPieceId;
}
