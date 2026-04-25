package nl.davefemi.data.dto.record;

import lombok.Data;

@Data
public class CastlingMoveRecordData implements MoveRecordData {
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
