package nl.davefemi.webchess.data.entity;

import lombok.Data;

@Data
public class PieceEntity {
    private int position;
    private String pieceType;
    private int pieceId;
    private String color;
}
