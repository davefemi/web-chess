package nl.davefemi.chess.data.dto.move;

import lombok.Data;
import nl.davefemi.chess.data.dto.MoveDTO;

@Data
public class MoveRequestDTO {
    private String playerId;
    private MoveDTO move;
}
