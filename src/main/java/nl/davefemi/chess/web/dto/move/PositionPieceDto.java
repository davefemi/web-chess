package nl.davefemi.chess.web.dto.move;

import lombok.Data;

@Data
public class PositionPieceDto {
    private String square;
    private String pieceType;
    private String color;
}
