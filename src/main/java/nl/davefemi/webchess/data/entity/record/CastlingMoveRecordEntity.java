package nl.davefemi.webchess.data.entity.record;

import lombok.Data;

@Data
public class CastlingMoveRecordEntity implements MoveRecordEntity {
    private int kingOldPosFile;
    private int kingOldPosRank;
    private int kingNewPosFile;
    private int kingNewPosRank;
    private int rookOldPosFile;
    private int rookOldPosRank;
    private int rookNewPosFile;
    private int rookNewPosRank;
    private String playerColor;
    private int kingId;
    private int rookId;
}
