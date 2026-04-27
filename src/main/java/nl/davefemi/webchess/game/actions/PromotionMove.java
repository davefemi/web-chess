package nl.davefemi.webchess.game.actions;

import nl.davefemi.webchess.game.board.PieceType;

public record PromotionMove(SingleMove move, PieceType newPieceType) implements Move {

}
