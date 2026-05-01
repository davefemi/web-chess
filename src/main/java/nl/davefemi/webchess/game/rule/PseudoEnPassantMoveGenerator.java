package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.actions.EnPassantMove;
import nl.davefemi.webchess.game.board.*;
import java.util.ArrayList;
import java.util.List;

public class PseudoEnPassantMoveGenerator {

    static List<EnPassantMove> generateMoves(Board board, Move lastMove, PieceColor color) {
        return getMoves(board, lastMove, BoardScanner.getCurrentPiecePositions(board, PieceType.PAWN, color));
    }

    private static List<EnPassantMove> getMoves(Board board, Move lastMove, List<Position> positions) {
        List<EnPassantMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position position : positions) {
                if (board.getPieceAt(position).getColor() == PieceColor.WHITE) {
                    int startingRank = 5;
                    int destinationRank = position.rank()+1;
                    pseudoMoves.addAll(getEnpassantMoves(board, lastMove, position, startingRank, destinationRank));
                }
                if (board.getPieceAt(position).getColor() == PieceColor.BLACK) {
                    int startingRank = 4;
                    int destinationRank = position.rank()-1;
                    pseudoMoves.addAll(getEnpassantMoves(board, lastMove, position, startingRank, destinationRank));                  }
                }
            }
        return pseudoMoves;
    }

    private static List<EnPassantMove> getEnpassantMoves(Board board, Move lastMove, Position position, int startingRank, int destinationRank){
        List<EnPassantMove> pseudoMoves = new ArrayList<>();
        List<Position> newPos = new ArrayList<>();
        if (position.rank() == startingRank){
            for (int file = Math.max(1, position.file()-1); file <= Math.min(8, position.file()+1); file++){
                Position enPassant = new Position(file, destinationRank);
                if (enPassant.file() != position.file())
                    newPos.add(enPassant);
            }
        }
        for (Position p : newPos) {
            if (MoveEvaluator.isEnpassantLegal(board, lastMove, position, p))
                pseudoMoves.add(new EnPassantMove(position, p));
        }
        return pseudoMoves;
    }
}
