package nl.davidfemi.domain.pieces;

import java.util.UUID;

public class Piece {
    private final UUID id;
    private final PieceType type;
    private final PlayerColor color;

    public Piece(PieceType type, PlayerColor color){
        id = UUID.randomUUID();
        this.type = type;
        this.color = color;
    }

    public UUID getId() {
        return id;
    }

    public PieceType getType() {
        return type;
    }

    public PlayerColor getColor() {
        return color;
    }
}
