package nl.davefemi.chess.web.message.dto.move;

import lombok.Data;

@Data
public class PositionPieceDto {
    private String square;
    private String pieceType;
    private String color;
}
