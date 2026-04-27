package nl.davefemi.webchess.data.dto.move;

import lombok.Data;

@Data
public class MoveRequestDTO {
    private String playerId;
    private MoveDTO move;
}
