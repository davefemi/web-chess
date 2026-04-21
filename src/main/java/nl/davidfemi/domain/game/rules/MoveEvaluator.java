package nl.davidfemi.domain.game.rules;

import nl.davidfemi.domain.board.Board;
import nl.davidfemi.domain.board.Position;
import nl.davidfemi.domain.game.utility.CastlingMove;
import nl.davidfemi.domain.game.utility.Move;
import nl.davidfemi.domain.game.utility.MoveGenerator;
import nl.davidfemi.domain.pieces.PlayerColor;

import java.util.List;

public final class MoveEvaluator {

    private MoveEvaluator(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static boolean isKingCheck(Board board, Position kingPosition, PlayerColor enemyColor){
        for (Move m: MoveGenerator.generateMoves(board, enemyColor, false)){
            if (m.to().equals(kingPosition))
                return true;
        }
        return false;
    }

    public static boolean evaluateOccupancy(Board board, Position oldPos, Position newPos, List<Move> moves){
        if (board.isOccupied(newPos)) {
            if (board.getPieceAt(oldPos).getColor() == board.getPieceAt(newPos).getColor())
                return true;
            moves.add(new Move(oldPos, newPos));
            return true;
        }
        moves.add(new Move(oldPos, newPos));
        return false;
    }

    public static Board fictitiousMove(Board board, Move move){
        Board newBoard = new Board(board);
        newBoard.movePieceTo(move);
        return newBoard;
    }

    public static Board fictitiousMove(Board board, CastlingMove move){
        Board newBoard = new Board(board);
        newBoard.movePieceTo(move);
        return newBoard;
    }
}
