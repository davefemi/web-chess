package nl.davefemi.data.dto.record;

import lombok.Data;

@Data
public class PromotionMoveRecordData implements MoveRecordData {
    private int posFile;
    private int posRank;
    private String playerColor;
    private String pieceType;
    private int pieceId;
}
