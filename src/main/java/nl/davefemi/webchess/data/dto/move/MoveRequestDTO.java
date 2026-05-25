package nl.davefemi.webchess.data.dto.move;

import lombok.Data;
import nl.davefemi.webchess.data.MoveDTO;

@Data
public class MoveRequestDTO {
    private String playerId;
    private MoveDTO move;
}
