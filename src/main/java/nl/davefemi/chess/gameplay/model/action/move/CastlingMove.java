package nl.davefemi.chess.gameplay.model.action.move;

public record CastlingMove(SingleMove moveKing, SingleMove moveRook) implements Move {
    public CastlingMove {
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
