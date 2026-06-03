package nl.davefemi.webchess.game.board;

import lombok.Getter;

@Getter
public final class Piece {
    private final int id;
    private final PieceType type;
    private final PieceColor color;

    public Piece(int id, PieceType type, PieceColor color){
        if(type == null)
            throw new IllegalArgumentException("Type cannot be null");
        if (color == null)
            throw new IllegalArgumentException("Color cannot be null");
        this.id = id;
        this.type = type;
        this.color = color;
    }
}
