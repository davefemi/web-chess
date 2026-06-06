package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SinglePseudoMoveGenerator {

    private SinglePseudoMoveGenerator(){
        throw new AssertionError("This class cannot be instantiated");
    }


    static List<SingleMove> generateMoves(Board board, PieceColor color) throws BoardException {
        List<SingleMove> moves = new ArrayList<>();
        moves.addAll(getKingMoves(board, board.getPositionsByTypeAndColor(PieceType.KING, color)));
        moves.addAll(getQueenMoves(board, board.getPositionsByTypeAndColor(PieceType.QUEEN, color)));
        moves.addAll(getBishopMoves(board, board.getPositionsByTypeAndColor(PieceType.BISHOP, color)));
        moves.addAll(getRookMoves(board, board.getPositionsByTypeAndColor(PieceType.ROOK, color)));
        moves.addAll(getKnightMoves(board, board.getPositionsByTypeAndColor(PieceType.KNIGHT, color)));
        moves.addAll(getPawnMoves(board, board.getPositionsByTypeAndColor(PieceType.PAWN, color), color));
        return moves;
    }

    private static List<SingleMove> getKingMoves(Board board, List<Square> positions) throws BoardException {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            Square king = positions.getFirst();
            for (int file = Math.max(king.file() - 1, 0); file <= Math.min(king.file() + 1, 7); file++) {
                for (int rank = Math.max(king.rank() - 1, 0); rank <= Math.min(king.rank() + 1, 7); rank++) {
                    Square newPos = Square.fromFileAndRank(file, rank);
                    if (!MoveEvaluator.filterForBlockingCapturePositions(board, king, newPos, pseudoMoves))
                        pseudoMoves.add(new SingleMove(king, newPos));
                }
            }
        }
        return pseudoMoves;
    }

    private static List<SingleMove> getQueenMoves(Board board, List<Square> positions) throws BoardException {
        List<SingleMove> legalMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Square position : positions) {
                legalMoves.addAll(getBishopMoves(board, Collections.singletonList(position)));
                legalMoves.addAll(getRookMoves(board, Collections.singletonList(position)));
            }
        }
        return legalMoves;
    }

    private static List<SingleMove> getBishopMoves(Board board, List<Square> positions) throws BoardException {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Square position : positions) {
                int incr = 1;
                for (int file = position.file(); file >= 1; file--) {
                    if (file - 1 < 0 || position.rank() - incr < 0) break;
                    Square newPos = Square.fromFileAndRank(file -1, position.rank() - incr);
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                    incr++;
                }
                incr = 1;
                for (int file = position.file(); file <= 6; file++) {
                    if (file + 1 > 7 || position.rank() + incr > 7) break;
                    Square newPos = Square.fromFileAndRank(file + 1, position.rank() + incr);
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                    incr++;
                }
                incr = 1;
                for (int file = Math.max(position.file(), 0); file >= 1; file--) {
                    if (file - 1 < 0 || position.rank() + incr > 7) break;
                    Square newPos = Square.fromFileAndRank(file - 1, position.rank() + incr);
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                    incr++;
                }
                incr = 1;
                for (int file = Math.min(position.file(), 7); file <= 6; file++) {
                    if (file + 1 > 7 || position.rank() - incr < 0) break;
                    Square newPos = Square.fromFileAndRank(file + 1, position.rank() - incr);
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                    incr++;
                }
            }
        }
        return pseudoMoves;
    }

    private static List<SingleMove> getKnightMoves(Board board, List<Square> positions) throws BoardException {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Square p : positions) {
                for (int file = Math.max(p.file() - 2, 0); file <= Math.min(p.file() + 2, 7); file++) {
                    for (int rank = Math.max(p.rank() - 2, 0); rank <= Math.min(p.rank() + 2, 7); rank++) {
                        int absFileDiff = Math.abs(file - p.file());
                        int absRankDiff = Math.abs(rank - p.rank());
                        if (absFileDiff == 2 && absRankDiff == 1 ||
                                absFileDiff == 1 && absRankDiff == 2) {
                            Square newPos = Square.fromFileAndRank(file, rank);
                            if(!MoveEvaluator.filterForBlockingCapturePositions(board, p, newPos, pseudoMoves))
                                pseudoMoves.add(new SingleMove(p, newPos));
                        }
                    }
                }
            }
        }
        return pseudoMoves;
    }

    private static List<SingleMove> getRookMoves(Board board, List<Square> positions) throws BoardException {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Square position : positions) {
                for (int file = Math.max(position.file() - 1, 0); file >= 0; file--) {
                    if (!rookMoveHasBeenGenerated(board, file, position.rank(), position, pseudoMoves)) break;
                }
                for (int file = Math.min(position.file() + 1, 7); file <= 7; file++) {
                    if (!rookMoveHasBeenGenerated(board, file, position.rank(), position, pseudoMoves)) break;
                }
                for (int rank = Math.max(position.rank() - 1, 0); rank >= 0; rank--) {
                    if (!rookMoveHasBeenGenerated(board, position.file(), rank, position, pseudoMoves)) break;
                }
                for (int rank = Math.min(position.rank() + 1, 7); rank <= 7; rank++) {
                    if (!rookMoveHasBeenGenerated(board, position.file(), rank, position, pseudoMoves)) break;
                }
            }
        }
        return pseudoMoves;
    }

    //TODO Test case to see if the last addition to pseudoMoves is redundant
    private static boolean rookMoveHasBeenGenerated(Board board, int file, int rank, Square position, List<SingleMove> pseudoMoves) throws BoardException {
        Square newPos = Square.fromFileAndRank(file, rank);
        if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) return false;
        pseudoMoves.add(new SingleMove(position, newPos));
        return true;
    }

    private static List<SingleMove> getPawnMoves(Board board, List<Square> positions, PieceColor color) throws BoardException {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Square position : positions) {
                boolean isWhite = color == PieceColor.WHITE;
                int movement = isWhite ? 1 : -1;
                int promotionRank = isWhite ? 7 : 0;
                int startingRank = isWhite ? 1: 6;
                for (Square p : generatePawnMoves(board, position, movement, promotionRank, startingRank)) {
                    if (MoveEvaluator.isPawnMoveLegal(board, position, p))
                        pseudoMoves.add(new SingleMove(position, p));
                }
            }
        }
        return pseudoMoves;
    }

    private static List<Square> generatePawnMoves(Board board, Square position, int movement, int promotionRank, int startingRank) throws BoardException {
        List<Square> newPos = new ArrayList<>();
        int bounds = position.rank() + movement;
        if (bounds < 8 && bounds > 0) {
            Square nextRank = Square.fromFileAndRank(position.file(), position.rank() + movement);
            if (position.rank() != promotionRank) {
                newPos.add(nextRank);
                if (position.file() - 1 >= 0)
                    newPos.add(Square.fromFileAndRank(position.file() - 1, position.rank() + movement));
                if (position.file() + 1 < 8)
                    newPos.add(Square.fromFileAndRank(position.file() + 1, position.rank() + movement));
            }
            if (position.rank() == startingRank) {
                if (MoveEvaluator.isPawnMoveLegal(board, position, nextRank))
                    newPos.add(Square.fromFileAndRank(position.file(), position.rank() + movement * 2));
            }
        }
        return newPos;
    }
}