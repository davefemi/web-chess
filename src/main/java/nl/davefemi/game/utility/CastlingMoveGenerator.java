package nl.davefemi.game.utility;

import nl.davefemi.game.board.Board;
import nl.davefemi.game.board.BoardScanner;
import nl.davefemi.game.board.Position;
import nl.davefemi.game.actions.CastlingMove;
import nl.davefemi.game.actions.SingleMove;
import nl.davefemi.game.rule.MoveEvaluator;
import nl.davefemi.game.board.PieceType;
import nl.davefemi.game.board.PieceColor;
import nl.davefemi.exception.BoardException;

import java.util.ArrayList;
import java.util.List;

public final class CastlingMoveGenerator {

    private CastlingMoveGenerator(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static List<CastlingMove> generateCastlingMoves(Board board, PieceColor color, boolean isActiveColor) throws BoardException {
        List<CastlingMove> pseudoMoves = new ArrayList<>();
        PieceColor enemyColor = color == PieceColor.WHITE ? PieceColor.BLACK : PieceColor.WHITE;
        Position king = BoardScanner.getCurrentSinglePiecePosition(board, PieceType.KING, color);
        List<Position> rooks = BoardScanner.getCurrentPiecePositions(board, PieceType.ROOK, color);
        for (CastlingMove c : getCastlingMoves(color)) {
            if (c.moveKing().from().equals(king)) {
                int lowerFile  = Math.min(c.moveKing().from().file(), c.moveRook().from().file());
                int higherFile = Math.max(c.moveKing().from().file(), c.moveRook().from().file());
                int rankKing = king.rank();
                for (Position p : rooks) {
                    if (c.moveRook().from().equals(p)) {
                        boolean empty = true;
                        for(int i = lowerFile+1; i<higherFile; i++){
                            if (board.isBoardPositionOccupied(new Position(i, rankKing)))
                                empty = false;
                        }
                        if (empty)
                            pseudoMoves.add(c);
                    }
                }
            }
        }

        if (isActiveColor) {
            return MoveEvaluator.filterAgainstCheckAfterCastling(board, pseudoMoves, enemyColor);
        }
        return pseudoMoves;
    }

    private static List<CastlingMove> getCastlingMoves(PieceColor color){
        List<CastlingMove>  moves = new ArrayList<>();
        int rank = 0;
        if (color == PieceColor.WHITE)
            rank = 1;
        if (color == PieceColor.BLACK)
            rank = 8;
        Position kStart = new Position(5, rank);
        Position kEnd1 = new Position(3, rank);
        Position kEnd2 = new Position(7, rank);
        Position rStart1 = new Position (1, rank);
        Position rEnd1 = new Position (4, rank);
        Position rStart2 = new Position (8, rank);
        Position rEnd2 = new Position (6, rank);
        moves.add(new CastlingMove(new SingleMove(kStart, kEnd1), new SingleMove(rStart1, rEnd1)));
        moves.add(new CastlingMove(new SingleMove(kStart, kEnd2), new SingleMove(rStart2, rEnd2)));
        return moves;
    }
}
