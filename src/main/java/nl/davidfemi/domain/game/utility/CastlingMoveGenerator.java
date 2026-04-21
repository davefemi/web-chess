package nl.davidfemi.domain.game.utility;

import nl.davidfemi.domain.board.Board;
import nl.davidfemi.domain.board.BoardScanner;
import nl.davidfemi.domain.board.Position;
import nl.davidfemi.domain.game.rules.MoveEvaluator;
import nl.davidfemi.domain.pieces.PieceType;
import nl.davidfemi.domain.pieces.PlayerColor;
import java.util.ArrayList;
import java.util.List;

public final class CastlingMoveGenerator {

    private CastlingMoveGenerator(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static List<CastlingMove> generateCastlingMoves(Board board, PlayerColor color) {
        List<CastlingMove> moves = new ArrayList<>();
        List<CastlingMove> pseudoMoves = new ArrayList<>();
        PlayerColor enemyColor = color == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE;
        Position king = BoardScanner.getPosition(board, PieceType.KING, color);
        List<Position> rooks = BoardScanner.getPositions(board, PieceType.ROOK, color);
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
        for (CastlingMove c : pseudoMoves) {
            if (!MoveEvaluator.isKingCheck(MoveEvaluator.fictitiousMove(board, c), c.moveKing().to(), enemyColor))
                moves.add(c);
        }
        return moves;
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
        moves.add(new CastlingMove(new Move(kStart, kEnd1), new Move(rStart1, rEnd1)));
        moves.add(new CastlingMove(new Move(kStart, kEnd2), new Move(rStart2, rEnd2)));
        return moves;
    }
}
