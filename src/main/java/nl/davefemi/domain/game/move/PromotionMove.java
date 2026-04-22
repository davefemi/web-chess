package nl.davefemi.domain.game.move;

import nl.davefemi.domain.board.Position;

public record PromotionMove(Position position, String newPiece) implements Move {

}
