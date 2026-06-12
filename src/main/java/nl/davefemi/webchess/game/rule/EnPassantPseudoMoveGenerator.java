package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.game.actions.move.EnPassantMove;
import nl.davefemi.webchess.game.actions.move.Move;
import nl.davefemi.webchess.game.board.*;
import java.util.ArrayList;
import java.util.List;

import static nl.davefemi.webchess.game.Color.*;
import static nl.davefemi.webchess.game.board.PieceType.PAWN;

public final class EnPassantPseudoMoveGenerator {

    private EnPassantPseudoMoveGenerator(){
        throw new AssertionError("This class cannot be instantiated");
    }


    static List<EnPassantMove> generateMoves(Board board, Move lastMove, Color color) throws BoardException {
        return getMoves(board, lastMove, board.getPositionsByTypeAndColor(PAWN, color), color);
    }

    //Index changed
    private static List<EnPassantMove> getMoves(Board board, Move lastMove, List<Square> positions, Color color)
            throws BoardException {
        List<EnPassantMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Square position : positions) {
                if (color == WHITE) {
                    int startingRank = 4;
                    int destinationRank = 5;
                    pseudoMoves.addAll(getEnpassantMoves(board, lastMove, position, startingRank, destinationRank, color));
                }
                if (color == BLACK) {
                    int startingRank = 3;
                    int destinationRank = 2;
                    pseudoMoves.addAll(getEnpassantMoves(board, lastMove, position, startingRank, destinationRank, color));                  }
                }
            }
        return pseudoMoves;
    }

    //Index changed
    private static List<EnPassantMove> getEnpassantMoves
    (Board board, Move lastMove, Square position, int startingRank, int destinationRank, Color color)
            throws BoardException {
        List<EnPassantMove> pseudoMoves = new ArrayList<>();
        List<Square> newPos = new ArrayList<>();
        if (position.rank() == startingRank){
            for (int file = Math.max(0, position.file()-1); file <= Math.min(7, position.file()+1); file++){
                if (file != position.file())
                    newPos.add(Square.fromFileAndRank(file, destinationRank));
            }
        }
        for (Square p : newPos) {
            if (MoveEvaluator.isEnpassantLegal(board, lastMove, position, p, color))
                pseudoMoves.add(new EnPassantMove(position, p));
        }
        return pseudoMoves;
    }
}
