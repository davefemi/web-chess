package nl.davefemi.webchess.data.dto.move;

import lombok.Data;
import nl.davefemi.webchess.data.MoveDTO;

@Data
public class CastlingMoveDTO implements MoveDTO {
    private SingleMoveDTO kingMove;
    private SingleMoveDTO rookMove;
}
