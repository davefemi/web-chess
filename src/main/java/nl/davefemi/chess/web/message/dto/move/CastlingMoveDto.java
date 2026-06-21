package nl.davefemi.chess.web.message.dto.move;

import lombok.Data;

@Data
public class CastlingMoveDto implements MoveDto {
    private String kingFrom;
    private String kingTo;
    private String rookFrom;
    private String rookTo;
}
