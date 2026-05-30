package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.move.EnPassantMove;
import nl.davefemi.webchess.game.actions.move.Move;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;

import java.util.ArrayList;
import java.util.List;

public final class AttackDetector {
    private AttackDetector(){
        throw new AssertionError("This class cannot be instantiated");
    }

    static boolean isPositionExposed(Board board, Move lastMove, Square position, PieceColor playingColor) throws BoardException {
        return !getEnemyMoves(board, lastMove, position, playingColor).isEmpty();
    }

    static List<Move> getEnemyMoves(Board board, Move lastMove, Square position, PieceColor playingColor) throws BoardException {
        List<Move> moves = new ArrayList<>();
        for (SingleMove s : PseudoSingleMoveGenerator.generateMoves(board, PieceColor.getOpponent(playingColor))){
            if (s.to().equals(position))
                moves.add(s);
        }
        if (lastMove!= null) {
            for (EnPassantMove e : PseudoEnPassantMoveGenerator.generateMoves(board, lastMove, PieceColor.getOpponent(playingColor))) {
                if (e.to().equals(position))
                    moves.add(e);
            }
        }
        return moves;
    }
}
