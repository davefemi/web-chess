package nl.davefemi.webchess.data.dto.move;

import lombok.Data;
import nl.davefemi.webchess.data.dto.MoveDTO;

@Data
public class MoveRequestDTO {
    private String playerId;
    private MoveDTO move;
}
