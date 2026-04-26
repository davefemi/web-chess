package nl.davefemi.data.dto.record;

import lombok.Data;

@Data
public class PromotionMoveRecordData implements MoveRecordData {
    private int oldPosFile;
    private int oldPosRank;
    private int newPosFile;
    private int newPosRank;
    private String playerColor;
    private int oldPieceId;
    private String newPieceType;
    private int newPieceId;

}
