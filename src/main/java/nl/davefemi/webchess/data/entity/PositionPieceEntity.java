package nl.davefemi.webchess.data.entity;

import lombok.Data;

@Data
public class PositionPieceEntity {
    private int positionValue;
    private String pieceType;
    private int pieceId;
    private String color;
}
