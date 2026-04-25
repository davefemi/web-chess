package nl.davefemi.game.rule;

import nl.davefemi.game.board.Board;
import nl.davefemi.game.board.Position;
import nl.davefemi.game.actions.PromotionMove;
import nl.davefemi.game.board.PieceType;
import nl.davefemi.game.board.PieceColor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.TypeException;

public final class PromotionMoveEvaluator {

    private PromotionMoveEvaluator(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static boolean checkForPromotion(Board board){
        for (Position p : board.getBoardPositions()){
            if (board.isBoardPositionOccupied(p) && board.getPieceAt(p).getType() == PieceType.PAWN &&
                    (p.rank() == 1 || p.rank() == 8 ))
                return true;
        }
        return false;
    }

    public static boolean isPromotionLegal(Board board, PromotionMove move) throws BoardException {
        PieceType type = move.newPiece();
        Position pawnPos = move.position();
        if (type == null)
            throw new TypeException("Invalid type");
        PieceColor color = board.getPieceAt(pawnPos).getColor();
        if (board.getPieceAt(pawnPos).getType() != PieceType.PAWN)
            throw new TypeException("Piece to be replaced is not a pawn");
        if (color == PieceColor.WHITE && pawnPos.rank() == 1 || color == PieceColor.BLACK && pawnPos.rank() == 8)
            throw new BoardException("This piece is not up for promotion");
        if (type == PieceType.PAWN || type == PieceType.KING)
            throw new TypeException("Replacement piece cannot be of type " + type.getLabel());
        return true;
    }

}
