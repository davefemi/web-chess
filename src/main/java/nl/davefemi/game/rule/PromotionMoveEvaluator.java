package nl.davefemi.game.rule;

import nl.davefemi.exception.MoveException;
import nl.davefemi.game.board.*;
import nl.davefemi.game.actions.PromotionMove;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.TypeException;

public final class PromotionMoveEvaluator {

    private PromotionMoveEvaluator(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static boolean isPromotionMoveLegal(Board board, PromotionMove move) throws MoveException {
        PieceType newPieceType = move.newPieceType();
        Position oldPawnPos = move.move().from();
        Piece piece = board.getPieceAt(oldPawnPos);
        if (piece == null){
            throw new MoveException("There is no piece at the position to be moved");
        }
        if (newPieceType == null)
            throw new TypeException("For promotion you have to specify a valid replacement type");
        PieceColor color = board.getPieceAt(oldPawnPos).getColor();
        if (board.getPieceAt(oldPawnPos).getType() != PieceType.PAWN)
            throw new TypeException("Piece to be replaced is not a pawn");
        if (color == PieceColor.WHITE && oldPawnPos.rank() != 7 ||
                color == PieceColor.BLACK && oldPawnPos.rank() != 2)
                throw new MoveException("This piece is not up for promotion");
        if (newPieceType == PieceType.PAWN || newPieceType == PieceType.KING)
            throw new TypeException("Replacement piece cannot be of type " + newPieceType.getLabel());
        return true;
    }
}
