package nl.davefemi.game.board;

public class Piece {
    private final int id;
    private final PieceType type;
    private final PieceColor color;

    public Piece(int id, PieceType type, PieceColor color){
        this.id = id;
        this.type = type;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public PieceType getType() {
        return type;
    }

    public PieceColor getColor() {
        return color;
    }
}
