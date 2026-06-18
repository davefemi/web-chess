package nl.davefemi.chess.http.dto.move;

import lombok.Data;
import nl.davefemi.chess.http.dto.MoveDto;

@Data
public class CastlingMoveDto implements MoveDto {
    private String kingFrom;
    private String kingTo;
    private String rookFrom;
    private String rookTo;
}
