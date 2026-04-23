package nl.davefemi.data.dto;

import lombok.Data;

@Data
public class PositionPieceDTO {
    private PositionDTO position;
    private String pieceType;
    private String color;
}
