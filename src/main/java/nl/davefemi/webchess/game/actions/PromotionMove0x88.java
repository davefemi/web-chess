package nl.davefemi.webchess.game.actions;

import nl.davefemi.webchess.game.board.PieceType;

public record PromotionMove0x88(SingleMove0x88 move, PieceType newPieceType) implements Move {
    public PromotionMove0x88 {
        if (move == null){
            throw new IllegalArgumentException("SingleMove cannot be null");
        }
        if (newPieceType == null){
            throw new IllegalArgumentException("New piece type  cannot be null");
        }
        if (!isPromotable(newPieceType)){
            throw new IllegalArgumentException(newPieceType + " is invalid for a promotion move");
        }
    }

    private boolean isPromotable(PieceType pieceType){
        return switch (pieceType) {
            case QUEEN, BISHOP, KNIGHT, ROOK -> true;
            case KING, PAWN -> false;
        };
    }
}
