package nl.davefemi.data.dto.move;

import lombok.Data;

@Data
public class MoveRequestDTO {
    private String playerId;
    private MoveDTO move;
}
