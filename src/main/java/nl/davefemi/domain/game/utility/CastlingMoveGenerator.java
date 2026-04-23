package nl.davefemi.domain.game.utility;

import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.board.BoardScanner;
import nl.davefemi.domain.board.Position;
import nl.davefemi.domain.game.move.CastlingMove;
import nl.davefemi.domain.game.move.SingleMove;
import nl.davefemi.domain.game.rule.MoveEvaluator;
import nl.davefemi.domain.piece.PieceType;
import nl.davefemi.domain.piece.PlayerColor;
import java.util.ArrayList;
import java.util.List;

public final class CastlingMoveGenerator {

    private CastlingMoveGenerator(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static List<CastlingMove> generateCastlingMoves(Board board, PlayerColor color, boolean isActiveColor) {
        List<CastlingMove> pseudoMoves = new ArrayList<>();
        PlayerColor enemyColor = color == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        Position king = BoardScanner.getCurrentSinglePosition(board, PieceType.KING, color);
        List<Position> rooks = BoardScanner.getCurrentPositions(board, PieceType.ROOK, color);
        for (CastlingMove c : getCastlingMoves(color)) {
            if (c.moveKing().from().equals(king)) {
                int lowerFile  = Math.min(c.moveKing().from().file(), c.moveRook().from().file());
                int higherFile = Math.max(c.moveKing().from().file(), c.moveRook().from().file());
                int rankKing = king.rank();
                for (Position p : rooks) {
                    if (c.moveRook().from().equals(p)) {
                        boolean empty = true;
                        for(int i = lowerFile+1; i<higherFile; i++){
                            if (board.isOccupied(new Position(i, rankKing)))
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

    private static List<CastlingMove> getCastlingMoves(PlayerColor color){
        List<CastlingMove>  moves = new ArrayList<>();
        int rank = 0;
        if (color == PlayerColor.WHITE)
            rank = 1;
        if (color == PlayerColor.BLACK)
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
