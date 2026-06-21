package nl.davefemi.chess.web.dto.request;

import lombok.Data;
import nl.davefemi.chess.web.dto.move.MoveDto;

@Data
public class MoveRequest {
    private MoveDto move;
}
