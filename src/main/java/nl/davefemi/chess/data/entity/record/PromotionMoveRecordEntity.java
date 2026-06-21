package nl.davefemi.chess.data.entity.record;

import lombok.Data;

@Data
public class PromotionMoveRecordEntity implements MoveRecordEntity {
    private int oldPos;
    private int newPos;
    private String playerColor;
    private String newPieceType;
    private int newPieceId;
    private String capturedPieceType;
    private int capturedPieceId;
}
