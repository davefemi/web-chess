package nl.davefemi.chess.http.dto.record;

import lombok.Data;
import nl.davefemi.chess.http.dto.MoveRecordDto;

@Data
public class CastlingMoveRecordDto implements MoveRecordDto {
    private String kingOldPos;
    private String kingNewPos;
    private String rookOldPos;
    private String rookNewPos;
    private String playerColor;
    private int kingId;
    private int rookId;
}
