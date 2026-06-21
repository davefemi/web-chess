package nl.davefemi.chess.web.message.request;

import lombok.Data;
import nl.davefemi.chess.web.message.dto.move.MoveDto;

@Data
public class MoveRequest {
    private MoveDto move;
}
