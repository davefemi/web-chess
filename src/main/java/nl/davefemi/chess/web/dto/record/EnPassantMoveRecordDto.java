package nl.davefemi.chess.web.dto.record;

import lombok.Data;

@Data
public class EnPassantMoveRecordDto implements MoveRecordDto {
    private String oldPos;
    private String newPos;
    private String playerColor;
    private String movedPieceType;
    private Integer movedPieceId;
    private String capturedPieceType;
    private Integer capturedPieceId;
}
