package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.board.Board;
import nl.davefemi.webchess.game.board.BoardScanner;
import nl.davefemi.webchess.game.board.Position;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.exception.BoardException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PseudoSingleMoveGenerator {

    static List<SingleMove> generateMoves(Board board, PieceColor color) {
        List<SingleMove> moves = new ArrayList<>();
        moves.addAll(getKingMoves(board, BoardScanner.getCurrentSinglePiecePosition(board, PieceType.KING, color)));
        moves.addAll(getQueenMoves(board, BoardScanner.getCurrentPiecePositions(board, PieceType.QUEEN, color)));
        moves.addAll(getBishopMoves(board, BoardScanner.getCurrentPiecePositions(board, PieceType.BISHOP, color)));
        moves.addAll(getRookMoves(board, BoardScanner.getCurrentPiecePositions(board, PieceType.ROOK, color)));
        moves.addAll(getKnightMoves(board, BoardScanner.getCurrentPiecePositions(board, PieceType.KNIGHT, color)));
        moves.addAll(getPawnMoves(board, BoardScanner.getCurrentPiecePositions(board, PieceType.PAWN, color), color));
        return moves;
    }

    private static List<SingleMove> getKingMoves(Board board, Position position) {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (position != null) {
            for (int file = Math.max(position.file() - 1, 1); file <= Math.min(position.file() + 1, 8); file++) {
                for (int rank = Math.max(position.rank() - 1, 1); rank <= Math.min(position.rank() + 1, 8); rank++) {
                    Position newPos = new Position(file, rank);
                    if (!MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves))
                        pseudoMoves.add(new SingleMove(position, newPos));
                }
            }
        }
        return pseudoMoves;
    }

    private static List<SingleMove> getQueenMoves(Board board, List<Position> positions) {
        List<SingleMove> legalMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position position : positions) {
                legalMoves.addAll(getBishopMoves(board, Collections.singletonList(position)));
                legalMoves.addAll(getRookMoves(board, Collections.singletonList(position)));
            }
        }
        return legalMoves;
    }

    private static List<SingleMove> getBishopMoves(Board board, List<Position> positions) {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position position : positions) {
                int incr = 1;
                for (int file = position.file() - 1; file >= 1; file--) {
                    Position newPos = new Position(file, position.rank() - incr);
                    if (newPos.file() < 1 || newPos.rank() < 1)
                        break;
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                    incr++;
                }
                incr = 1;
                for (int file = position.file() + 1; file <= 8; file++) {
                    Position newPos = new Position(file, position.rank() + incr);
                    if (newPos.file() > 8 || newPos.rank() > 8)
                        break;
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                    incr++;
                }
                incr = 1;
                for (int file = Math.max(position.file() - 1, 1); file >= 1; file--) {
                    Position newPos = new Position(file, position.rank() + incr);
                    if (newPos.file() < 1 || newPos.rank() > 8)
                        break;
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                    incr++;
                }
                incr = 1;
                for (int file = Math.min(position.file() + 1, 8); file <= 8; file++) {
                    Position newPos = new Position(file, position.rank() - incr);
                    if (newPos.file() > 8 || newPos.rank() < 1)
                        break;
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                    incr++;
                }
            }
        }
        return pseudoMoves;
    }

    private static List<SingleMove> getKnightMoves(Board board, List<Position> positions) {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position p : positions) {
                for (int file = Math.max(p.file() - 2, 1); file <= Math.min(p.file() + 2, 8); file++) {
                    for (int rank = Math.max(p.rank() - 2, 1); rank <= Math.min(p.rank() + 2, 8); rank++) {
                        int absFileDiff = Math.abs(file - p.file());
                        int absRankDiff = Math.abs(rank - p.rank());
                        if (absFileDiff == 2 && absRankDiff == 1 ||
                                absFileDiff == 1 && absRankDiff == 2) {
                            Position newPos = new Position(file, rank);
                            if(!MoveEvaluator.filterForBlockingCapturePositions(board, p, newPos, pseudoMoves))
                                pseudoMoves.add(new SingleMove(p, newPos));
                        }
                    }
                }
            }
        }
        return pseudoMoves;
    }

    private static List<SingleMove> getRookMoves(Board board, List<Position> positions) {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position position : positions) {
                for (int file = Math.max(position.file() - 1, 1); file >= 1; file--) {
                    if (!rookMoveHasBeenGenerated(board, file, position.rank(), position, pseudoMoves)) break;
                }
                for (int file = Math.min(position.file() + 1, 8); file <= 8; file++) {
                    if (!rookMoveHasBeenGenerated(board, file, position.rank(), position, pseudoMoves)) break;
                }
                for (int rank = Math.max(position.rank() - 1, 1); rank >= 1; rank--) {
                    if (!rookMoveHasBeenGenerated(board, position.file(), rank, position, pseudoMoves)) break;
                }
                for (int rank = Math.min(position.rank() + 1, 8); rank <= 8; rank++) {
                    if (!rookMoveHasBeenGenerated(board, position.file(), rank, position, pseudoMoves)) break;
                }
            }
        }
        return pseudoMoves;
    }

    //TODO Test case to see if the last addition to pseudoMoves is redundant
    private static boolean rookMoveHasBeenGenerated(Board board, int file, int rank, Position position, List<SingleMove> pseudoMoves){
        Position newPos = new Position(file, rank);
        if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) return false;
        pseudoMoves.add(new SingleMove(position, newPos));
        return true;
    }

    private static List<SingleMove> getPawnMoves(Board board,List<Position> positions, PieceColor color) {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position position : positions) {
                boolean isWhite = color == PieceColor.WHITE;
                int movement = isWhite ? 1 : -1;
                int promotionRank = isWhite ? 8 : 1;
                int startingRank = isWhite ? 2: 7;
                for (Position p : generatePawnMoves(board, position, movement, promotionRank, startingRank)) {
                    if (MoveEvaluator.isPawnMoveLegal(board, position, p))
                        pseudoMoves.add(new SingleMove(position, p));
                }
            }
        }
        return pseudoMoves;
    }

    private static List<Position> generatePawnMoves(Board board, Position position, int movement, int promotionRank, int startingRank) {
        List<Position> newPos = new ArrayList<>();
        Position nextRank = new Position(position.file(), position.rank() + movement);
        if (position.rank() != promotionRank) {
            newPos.add(nextRank);
            if (position.file() - 1 > 0)
                newPos.add(new Position(position.file() - 1, position.rank() + movement));
            if (position.file() + 1 < 9)
                newPos.add(new Position(position.file() + 1, position.rank() + movement));
        }
        if (position.rank() == startingRank) {
            if (MoveEvaluator.isPawnMoveLegal(board, position, nextRank))
                newPos.add(new Position(position.file(), position.rank() + movement * 2));
        }
        return newPos;
    }

    static Board applyFictitiousMove(Board board, Move move) throws BoardException {
        Board newBoard = new Board(board);
        newBoard.applyValidatedMove(move);
        return newBoard;
    }
}