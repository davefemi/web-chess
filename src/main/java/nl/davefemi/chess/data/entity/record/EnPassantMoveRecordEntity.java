package nl.davefemi.chess.data.entity.record;

import lombok.Data;

@Data
public class EnPassantMoveRecordEntity implements MoveRecordEntity {
    private int oldPos;
    private int newPos;
    private String playerColor;
    private String movedPieceType;
    private int movedPieceId;
    private String capturedPieceType;
    private int capturedPieceId;
}
