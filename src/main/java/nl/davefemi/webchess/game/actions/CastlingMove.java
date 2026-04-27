package nl.davefemi.webchess.game.actions;

public record CastlingMove(SingleMove moveKing, SingleMove moveRook) implements Move {
}
