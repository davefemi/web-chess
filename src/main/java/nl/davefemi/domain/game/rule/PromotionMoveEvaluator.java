package nl.davefemi.domain.game.rule;

import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.board.Position;
import nl.davefemi.domain.game.actions.move.PromotionMove;
import nl.davefemi.domain.board.PieceType;
import nl.davefemi.domain.board.PlayerColor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.TypeException;

public final class PromotionMoveEvaluator {

    private PromotionMoveEvaluator(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static boolean checkForPromotion(Board board){
        for (Position p : board.getPositions()){
            if (board.isOccupied(p) && board.getPieceAt(p).getType() == PieceType.PAWN &&
                    (p.rank() == 1 || p.rank() == 8 ))
                return true;
        }
        return false;
    }

    public static boolean isPromotionLegal(Board board, PromotionMove move) throws BoardException {
        PieceType type = board.getPieceType(move.newPiece());
        Position pawnPos = move.position();
        if (type == null)
            throw new TypeException("Invalid type");
        PlayerColor color = board.getPieceAt(pawnPos).getColor();
        if (board.getPieceAt(pawnPos).getType() != PieceType.PAWN)
            throw new TypeException("Piece to be replaced is not a pawn");
        if (color == PlayerColor.WHITE && pawnPos.rank() == 1 || color == PlayerColor.BLACK && pawnPos.rank() == 8)
            throw new BoardException("This piece is not up for promotion");
        if (type == PieceType.PAWN || type == PieceType.KING)
            throw new TypeException("Replacement piece cannot be of type " + type.getLabel());
        return true;
    }

}
