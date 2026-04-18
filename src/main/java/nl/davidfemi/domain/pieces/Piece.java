package nl.davidfemi.domain.pieces;

public class Piece {
    private final PieceType type;
    private final PlayerColor color;

    public Piece(PieceType type, PlayerColor color){
        this.type = type;
        this.color = color;
    }

    public String getType() {
        return type.getLabel();
    }

    public PlayerColor getColor() {
        return color;
    }
}
