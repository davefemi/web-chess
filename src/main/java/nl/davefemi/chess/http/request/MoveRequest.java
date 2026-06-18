package nl.davefemi.chess.http.request;

import lombok.Data;
import nl.davefemi.chess.http.dto.MoveDto;

@Data
public class MoveRequest {
    private MoveDto move;
}
