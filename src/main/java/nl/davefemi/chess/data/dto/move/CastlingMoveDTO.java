package nl.davefemi.chess.data.dto.move;

import lombok.Data;
import nl.davefemi.chess.data.dto.MoveDTO;

@Data
public class CastlingMoveDTO implements MoveDTO {
    private String kingFrom;
    private String kingTo;
    private String rookFrom;
    private String rookTo;
}
