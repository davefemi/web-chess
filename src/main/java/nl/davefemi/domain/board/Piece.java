package nl.davefemi.domain.board;

public class Piece {
    private final int id;
    private final PieceType type;
    private final PlayerColor color;

    public Piece(int id, PieceType type, PlayerColor color){
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

    public PlayerColor getColor() {
        return color;
    }
}
