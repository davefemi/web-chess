package nl.davefemi.webchess.game.actions.move;

import nl.davefemi.webchess.game.board.PieceType;

public record PromotionMove(SingleMove move, PieceType newPieceType) implements SinglePieceMove {
    public PromotionMove {
        if (newPieceType == null){
            throw new IllegalArgumentException("New piece type  cannot be null");
        }
        if (!isPromotable(newPieceType)){
            throw new IllegalArgumentException(newPieceType + " is an invalid type for a replacement");
        }
    }

    private boolean isPromotable(PieceType pieceType){
        return switch (pieceType) {
            case QUEEN, BISHOP, KNIGHT, ROOK -> true;
            case KING, PAWN -> false;
        };
    }
}
