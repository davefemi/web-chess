package nl.davidfemi.data.dto;

import lombok.Data;

@Data
public class CastlingMoveDTO {
    private MoveDTO kingMove;
    private MoveDTO rookMove;
}
