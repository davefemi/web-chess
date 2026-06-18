package nl.davefemi.chess.data.dto.move;

import lombok.Data;
import nl.davefemi.chess.data.dto.MoveDTO;

@Data
public class SingleMoveDTO implements MoveDTO {
    private String from;
    private String to;
}
