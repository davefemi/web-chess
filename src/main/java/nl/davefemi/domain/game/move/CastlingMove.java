package nl.davefemi.domain.game.move;

public record CastlingMove(SingleMove moveKing, SingleMove moveRook) implements Move {
}
