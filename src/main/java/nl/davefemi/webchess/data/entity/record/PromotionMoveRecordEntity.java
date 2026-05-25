package nl.davefemi.webchess.data.entity.record;

import lombok.Data;

@Data
public class PromotionMoveRecordEntity implements MoveRecordEntity {
    private int oldPosFile;
    private int oldPosRank;
    private int newPosFile;
    private int newPosRank;
    private String playerColor;
    private int oldPieceId;
    private String newPieceType;
    private int newPieceId;

}
