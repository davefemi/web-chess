package nl.davefemi.webchess.game.board;

import lombok.Getter;

@Getter
public final class Piece {
    private final int id;
    private final PieceType type;
    private final PieceColor color;

    public Piece(int id, PieceType type, PieceColor color){
        this.id = id;
        this.type = type;
        this.color = color;
    }
}
