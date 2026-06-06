package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.move.CastlingMove;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;
import java.util.ArrayList;
import java.util.List;

public final class PseudoCastlingMoveGenerator {

    private PseudoCastlingMoveGenerator(){
        throw new AssertionError("This class cannot be instantiated");
    }

    static List<CastlingMove> generateMoves(Board board, PieceColor color) throws BoardException {
        List<CastlingMove> pseudoMoves = new ArrayList<>();
        List<Square> positions = board.getPositionsByTypeAndColor(PieceType.KING, color);
        if (!positions.isEmpty()) {
            Square king = positions.getFirst();
            List<Square> rooks = board.getPositionsByTypeAndColor(PieceType.ROOK, color);
            for (CastlingMove c : getMoves(color)) {
                if (c.moveKing().from().equals(king)) {
                    int lowerFile = Math.min(c.moveKing().from().file(), c.moveRook().from().file());
                    int higherFile = Math.max(c.moveKing().from().file(), c.moveRook().from().file());
                    int rankKing = king.rank();
                    for (Square p : rooks) {
                        if (c.moveRook().from().equals(p)) {
                            boolean empty = true;
                            for (int i = lowerFile + 1; i < higherFile; i++) {
                                if (board.isPositionOccupied(Square.fromFileAndRank(i, rankKing)))
                                    empty = false;
                            }
                            if (empty)
                                pseudoMoves.add(c);
                        }
                    }
                }
            }
        }
        return pseudoMoves;
    }

    //Index changed
    private static List<CastlingMove> getMoves(PieceColor color){
        List<CastlingMove>  moves = new ArrayList<>();
        int rank = 0;
        if (color == PieceColor.BLACK)
            rank = 7;
        Square kStart = Square.fromFileAndRank(4, rank);
        Square kEnd1 = Square.fromFileAndRank(2, rank);
        Square kEnd2 = Square.fromFileAndRank(6, rank);
        Square rStart1 = Square.fromFileAndRank (0, rank);
        Square rEnd1 = Square.fromFileAndRank (3, rank);
        Square rStart2 = Square.fromFileAndRank (7, rank);
        Square rEnd2 = Square.fromFileAndRank (5, rank);
        moves.add(new CastlingMove(new SingleMove(kStart, kEnd1), new SingleMove(rStart1, rEnd1)));
        moves.add(new CastlingMove(new SingleMove(kStart, kEnd2), new SingleMove(rStart2, rEnd2)));
        return moves;
    }
}
