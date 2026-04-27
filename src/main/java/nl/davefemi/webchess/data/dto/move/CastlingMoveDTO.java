package nl.davefemi.webchess.data.dto.move;

import lombok.Data;

@Data
public class CastlingMoveDTO implements MoveDTO {
    private SingleMoveDTO kingMove;
    private SingleMoveDTO rookMove;
}
