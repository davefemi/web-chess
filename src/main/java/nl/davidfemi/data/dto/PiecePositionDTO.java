package nl.davidfemi.data.dto;

import lombok.Data;

@Data
public class PiecePositionDTO {
    private PositionDTO position;
    private String pieceType;
    private String color;
}
