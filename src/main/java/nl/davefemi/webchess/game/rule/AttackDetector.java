package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.game.actions.EnPassantMove;
import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.Board;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.board.Position;

import java.util.ArrayList;
import java.util.List;

public final class AttackDetector {
    private AttackDetector(){
        throw new AssertionError("This class cannot be instantiated");
    }

    static boolean isPositionExposed(Board board, Move lastMove, Position position, PieceColor playingColor) {
        return !getEnemyMoves(board, lastMove, position, playingColor).isEmpty();
    }

    static List<Move> getEnemyMoves(Board board, Move lastMove, Position position, PieceColor playingColor){
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
