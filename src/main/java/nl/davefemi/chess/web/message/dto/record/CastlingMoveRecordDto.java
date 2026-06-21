package nl.davefemi.chess.web.message.dto.record;

import lombok.Data;

@Data
public class CastlingMoveRecordDto implements MoveRecordDto {
    private String kingOldPos;
    private String kingNewPos;
    private String rookOldPos;
    private String rookNewPos;
    private String playerColor;
    private Integer kingId;
    private Integer rookId;
}
