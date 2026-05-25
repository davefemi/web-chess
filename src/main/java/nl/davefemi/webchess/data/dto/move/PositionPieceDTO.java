package nl.davefemi.webchess.data.dto.move;

import lombok.Data;

@Data
public class PositionPieceDTO {
    private String square;
    private String pieceType;
    private String color;
}
