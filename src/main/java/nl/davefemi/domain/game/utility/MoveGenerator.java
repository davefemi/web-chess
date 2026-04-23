package nl.davefemi.domain.game.utility;

import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.board.BoardScanner;
import nl.davefemi.domain.board.Position;
import nl.davefemi.domain.game.actions.move.SingleMove;
import nl.davefemi.domain.game.rule.MoveEvaluator;
import nl.davefemi.domain.board.PieceType;
import nl.davefemi.domain.board.PlayerColor;
import nl.davefemi.exception.BoardException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MoveGenerator {

    public static List<SingleMove> generateMoves(Board board, PlayerColor color, boolean isActiveColor) throws BoardException {
        List<SingleMove> moves = new ArrayList<>();
        PlayerColor enemyColor =
                color == PlayerColor.WHITE
                ?PlayerColor.BLACK
                :PlayerColor.WHITE;
        moves.addAll(getKingMoves(board, BoardScanner.getCurrentSinglePosition(board, PieceType.KING, color), enemyColor, isActiveColor));
        moves.addAll(getQueenMoves(board, BoardScanner.getCurrentPositions(board, PieceType.QUEEN, color), enemyColor, isActiveColor));
        moves.addAll(getBishopMoves(board, BoardScanner.getCurrentPositions(board, PieceType.BISHOP, color), enemyColor, isActiveColor));
        moves.addAll(getRookMoves(board, BoardScanner.getCurrentPositions(board, PieceType.ROOK, color), enemyColor, isActiveColor));
        moves.addAll(getKnightMoves(board, BoardScanner.getCurrentPositions(board, PieceType.KNIGHT, color), enemyColor, isActiveColor));
        moves.addAll(getPawnMoves(board, BoardScanner.getCurrentPositions(board, PieceType.PAWN, color), enemyColor, isActiveColor));
        return moves;
    }

    private static List<SingleMove> getKingMoves(Board board, Position position, PlayerColor enemyColor, boolean isActiveColor) throws BoardException {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (position != null) {
            for (int file = Math.max(position.file() - 1, 1); file <= Math.min(position.file() + 1, 8); file++) {
                for (int rank = Math.max(position.rank() - 1, 1); rank <= Math.min(position.rank() + 1, 8); rank++) {
                    Position newPos = new Position(file, rank);
                    if (!MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves))
                        pseudoMoves.add(new SingleMove(position, newPos));
                }
            }
            if (isActiveColor) {
                return MoveEvaluator.filterAgainstCheckAfterMove(board, pseudoMoves, enemyColor);
            }
        }
        return pseudoMoves;
    }

    private static List<SingleMove> getQueenMoves(Board board, List<Position> positions, PlayerColor enemyColor, boolean isActiveColor) throws BoardException {
        List<SingleMove> legalMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position position : positions) {
                legalMoves.addAll(getBishopMoves(board, Collections.singletonList(position), enemyColor, isActiveColor));
                legalMoves.addAll(getRookMoves(board, Collections.singletonList(position), enemyColor, isActiveColor));
            }
        }
        return legalMoves;
    }

    private static List<SingleMove> getBishopMoves(Board board, List<Position> positions, PlayerColor enemyColor, boolean isActiveColor) throws BoardException {
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
        if (isActiveColor) {
            return MoveEvaluator.filterAgainstCheckAfterMove(board, pseudoMoves, enemyColor);
        }
        return pseudoMoves;
    }

    private static List<SingleMove> getKnightMoves(Board board, List<Position> positions, PlayerColor enemyColor, boolean isActiveColor) throws BoardException {
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
        if (isActiveColor) {
            return MoveEvaluator.filterAgainstCheckAfterMove(board, pseudoMoves, enemyColor);
        }
        return pseudoMoves;
    }

    private static List<SingleMove> getRookMoves(Board board, List<Position> positions, PlayerColor enemyColor, boolean isActiveColor) throws BoardException {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position position : positions) {
                for (int file = Math.max(position.file() - 1, 1); file >= 1; file--) {
                    Position newPos = new Position(file, position.rank());
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                }
                for (int file = Math.min(position.file() + 1, 8); file <= 8; file++) {
                    Position newPos = new Position(file, position.rank());
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                }
                for (int rank = Math.max(position.rank() - 1, 1); rank >= 1; rank--) {
                    Position newPos = new Position(position.file(), rank);
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                }
                for (int rank = Math.min(position.rank() + 1, 8); rank <= 8; rank++) {
                    Position newPos = new Position(position.file(), rank);
                    if (MoveEvaluator.filterForBlockingCapturePositions(board, position, newPos, pseudoMoves)) break;
                    pseudoMoves.add(new SingleMove(position, newPos));
                }
            }
        }
        if (isActiveColor) {
            return MoveEvaluator.filterAgainstCheckAfterMove(board, pseudoMoves, enemyColor);
        }
        return pseudoMoves;
    }

    private static List<SingleMove> getPawnMoves(Board board, List<Position> positions, PlayerColor enemyColor, boolean isActiveColor) throws BoardException {
        List<SingleMove> pseudoMoves = new ArrayList<>();
        if (!positions.isEmpty()) {
            for (Position position : positions) {
                List<Position> newPos = new ArrayList<>();
                if (board.getPieceAt(position).getColor() == PlayerColor.WHITE) {
                    if (position.rank() < 8) {
                        newPos.add(new Position(position.file(), position.rank() + 1));
                        if (position.file() - 1 > 0)
                            newPos.add(new Position(position.file() - 1, position.rank() + 1));
                        if (position.file() + 1 < 9)
                            newPos.add(new Position(position.file() + 1, position.rank() + 1));
                    }
                    if (position.rank() == 2)
                        newPos.add(new Position(position.file(), position.rank() + 2));
                }
                if (board.getPieceAt(position).getColor() == PlayerColor.BLACK) {
                    if (position.rank() > 1) {
                        newPos.add(new Position(position.file(), position.rank() - 1));
                        if (position.file() - 1 > 0)
                            newPos.add(new Position(position.file() - 1, position.rank() - 1));
                        if (position.file() + 1 < 9)
                            newPos.add(new Position(position.file() + 1, position.rank() - 1));
                    }
                    if (position.rank() == 7)
                        newPos.add(new Position(position.file(), position.rank() - 2));
                }
                for (Position p : newPos) {
                    if (MoveEvaluator.isPawnMoveLegal(board, position, p))
                        pseudoMoves.add(new SingleMove(position, p));
                }
            }
        }
        if (isActiveColor) {
            return MoveEvaluator.filterAgainstCheckAfterMove(board, pseudoMoves, enemyColor);
        }
        return pseudoMoves;
    }
}