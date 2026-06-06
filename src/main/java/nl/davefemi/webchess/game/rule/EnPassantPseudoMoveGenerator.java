package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.move.EnPassantMove;
import nl.davefemi.webchess.game.actions.move.Move;
import nl.davefemi.webchess.game.board.*;
import java.util.ArrayList;
import java.util.List;

public class EnPassantPseudoMoveGenerator {

    private EnPassantPseudoMoveGenerator(){
        throw new AssertionError("This class cannot be instantiated");
    }


    static List<EnPassantMove> generateMoves(Board board, Move lastMove, PieceColor color) throws BoardException {
        return getMoves(board, lastMove, board.getPositionsByTypeAndColor(PieceType.PAWN, color), color);
    }

    //Index changed
    private static List<EnPassantMove> getMoves(Board board, Move lastMove, List<Square> positions, PieceColor color) throws BoardException {
        List<EnPassantMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Square position : positions) {
                if (color == PieceColor.WHITE) {
                    int startingRank = 4;
                    int destinationRank = position.rank()+1;
                    pseudoMoves.addAll(getEnpassantMoves(board, lastMove, position, startingRank, destinationRank, color));
                }
                if (color == PieceColor.BLACK) {
                    int startingRank = 3;
                    int destinationRank = position.rank()-1;
                    pseudoMoves.addAll(getEnpassantMoves(board, lastMove, position, startingRank, destinationRank, color));                  }
                }
            }
        return pseudoMoves;
    }

    //Index changed
    private static List<EnPassantMove> getEnpassantMoves(Board board, Move lastMove, Square position, int startingRank, int destinationRank, PieceColor color) throws BoardException {
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
