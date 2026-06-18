package nl.davefemi.chess.data.entity.record;

import lombok.Data;
import nl.davefemi.chess.data.entity.MoveRecordEntity;

@Data
public class SingleMoveRecordEntity implements MoveRecordEntity {
    private int oldPos;
    private int newPos;
    private String playerColor;
    private String movedPieceType;
    private int movedPieceId;
    private String capturedPieceType;
    private int capturedPieceId;
}
