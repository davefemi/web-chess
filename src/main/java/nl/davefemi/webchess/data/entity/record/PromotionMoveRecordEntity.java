package nl.davefemi.webchess.data.entity.record;

import lombok.Data;
import nl.davefemi.webchess.data.MoveRecordEntity;

@Data
public class PromotionMoveRecordEntity implements MoveRecordEntity {
    private int oldPos;
    private int newPos;
    private String playerColor;
    private String oldPieceType;
    private int oldPieceId;
    private String newPieceType;
    private int newPieceId;
    private String capturedPieceType;
    private int capturedPieceId;
}
