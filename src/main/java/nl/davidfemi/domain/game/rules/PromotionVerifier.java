package nl.davidfemi.domain.game.rules;

import nl.davidfemi.domain.board.Board;
import nl.davidfemi.domain.board.Position;
import nl.davidfemi.domain.pieces.PieceType;
import nl.davidfemi.domain.pieces.PlayerColor;
import nl.davidfemi.exception.BoardException;
import nl.davidfemi.exception.TypeException;

public final class PromotionVerifier {

    private PromotionVerifier(){
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

    public static boolean isPromotionLegal(Board board, Position pawn, PieceType pieceType){
        PlayerColor color = board.getPieceAt(pawn).getColor();
        if (board.getPieceAt(pawn).getType() != PieceType.PAWN)
            throw new TypeException("Piece to be replaced is not a pawn");
        if (! (color == PlayerColor.WHITE && pawn.rank() == 8 || color == PlayerColor.BLACK && pawn.rank() == 1))
            throw new BoardException("Piece is not up for promotion");
        if (pieceType == PieceType.PAWN || pieceType == PieceType.KING)
            throw new TypeException("Replacement piece cannot be of type " + pieceType.getLabel());
        return true;
    }

}
