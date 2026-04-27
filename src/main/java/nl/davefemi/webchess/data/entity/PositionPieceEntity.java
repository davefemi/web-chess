package nl.davefemi.webchess.data.entity;

import lombok.Data;

@Data
public class PositionPieceEntity {
    private int file;
    private int rank;
    private String pieceType;
    private int pieceId;
    private String color;
}
