package nl.davefemi.chess.data.entity.record;

import lombok.Data;
import nl.davefemi.chess.data.entity.MoveRecordEntity;

@Data
public class CastlingMoveRecordEntity implements MoveRecordEntity {
    private int kingOldPos;
    private int kingNewPos;
    private int rookOldPos;
    private int rookNewPos;
    private String playerColor;
    private int kingId;
    private int rookId;
}
