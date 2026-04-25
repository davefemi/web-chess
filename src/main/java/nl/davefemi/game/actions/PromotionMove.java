package nl.davefemi.game.actions;

import nl.davefemi.game.board.PieceType;
import nl.davefemi.game.board.Position;

public record PromotionMove(Position position, PieceType newPiece) implements Move {

}
