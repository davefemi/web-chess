package nl.davefemi.data.dto;

import lombok.Data;

@Data
public class CastlingMoveDTO implements MoveDTO {
    private SingleMoveDTO king_move;
    private SingleMoveDTO rook_move;
}
