package nl.davefemi.webchess.data.entity.record;

import lombok.Data;

@Data
public class SingleMoveRecordEntity implements MoveRecordEntity {
    private int oldPosFile;
    private int oldPosRank;
    private int newPosFile;
    private int newPosRank;
    private String playerColor;
    private String movedPieceType;
    private int movedPieceId;
    private String capturedPieceType;
    private int capturedPieceId;
}
