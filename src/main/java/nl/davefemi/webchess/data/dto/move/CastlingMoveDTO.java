package nl.davefemi.webchess.data.dto.move;

import lombok.Data;
import nl.davefemi.webchess.data.MoveDTO;

@Data
public class CastlingMoveDTO implements MoveDTO {
    private String kingFrom;
    private String kingTo;
    private String rookFrom;
    private String rookTo;
}
