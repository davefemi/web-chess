package nl.davefemi.webchess.data.dto.record;

import lombok.Data;
import nl.davefemi.webchess.data.dto.MoveRecordDTO;

@Data
public class CastlingMoveRecordDTO implements MoveRecordDTO {
    private String kingOldPos;
    private String kingNewPos;
    private String rookOldPos;
    private String rookNewPos;
    private String playerColor;
    private int kingId;
    private int rookId;
}
