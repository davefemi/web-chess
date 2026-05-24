package nl.davefemi.webchess.game.actions;

public record CastlingMove0x88(SingleMove0x88 moveKing, SingleMove0x88 moveRook) implements Move {
    public CastlingMove0x88 {
        if (moveKing == null) {
            throw new IllegalArgumentException("King move cannot be null");
        }
        if (moveRook == null) {
            throw new IllegalArgumentException("Rook move cannot be null");
        }
        if (moveKing.equals(moveRook)){
            throw new IllegalArgumentException("King and Rook cannot have identical moves");
        }
    }
}
