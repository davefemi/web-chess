package nl.davefemi.game.actions;

public record CastlingMove(SingleMove moveKing, SingleMove moveRook) implements Move {
}
