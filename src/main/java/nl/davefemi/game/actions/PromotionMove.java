package nl.davefemi.game.actions;

import nl.davefemi.game.board.PieceType;

public record PromotionMove(SingleMove move, PieceType newPieceType) implements Move {

}
