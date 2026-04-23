package nl.davefemi.domain.game.actions.move;

public record CastlingMove(SingleMove moveKing, SingleMove moveRook) implements Move {
}
