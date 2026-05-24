package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.game.actions.*;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.record.MoveRecord;

import java.util.*;

public final class RuleEngine {

    private RuleEngine() {
        throw new AssertionError("This class cannot be instantiated");
    }

    public static boolean isMoveAllowed(BoardContext boardContext, List<MoveRecord> moveHistory, PieceColor pieceColor, Move move) throws BoardException, MoveException {
        if (!(pieceColor == boardContext.getPlayerToMove()))
            throw new MoveException("It is not " + pieceColor + "'s turn yet");
        if (move instanceof CastlingMove m)
            if (!(MoveEvaluator.isCastlingMoveLegal(boardContext.getCopyOfBoard(), boardContext.getOriginalRooks(), moveHistory, m)))
                throw new MoveException("Castling is not allowed");
        if (move instanceof SingleMove m) {
            Piece p = boardContext.getCopyOfBoard().getPieceAt(m.from());
            if (p != null && p.getType() == PieceType.PAWN)
                if ((p.getColor() == PieceColor.WHITE && m.from().rank() == 7 ||
                        p.getColor() == PieceColor.BLACK && m.from().rank() == 2)) {
                    throw new MoveException("Single move not allowed here, use promotion move");
                }
        }
        if (move instanceof PromotionMove m) {
            if (!MoveEvaluator.isPromotionMoveLegal(boardContext.getCopyOfBoard(), m))
                throw new MoveException("Promotion not allowed");
            move = m.move();
        }
        return getAllLegalMovesByPieceColor(boardContext, pieceColor).contains(move);
    }

    public static List<Move> getAllLegalMovesByPieceColor(BoardContext boardContext, PieceColor color) throws BoardException {
        List<Move> moves = new ArrayList<>();
        Board board = boardContext.getCopyOfBoard();
        Move lastMove = boardContext.getLastMove() != null ? boardContext.getLastMove().getMove() : null;
        moves.addAll(PseudoSingleMoveGenerator.generateMoves(board, color));
        moves.addAll(PseudoCastlingMoveGenerator.generateMoves(board, color));
        moves.addAll(PseudoEnPassantMoveGenerator.generateMoves(board, lastMove, color));
        return MoveEvaluator.evaluateIfKingIsInCheckAfterMove(boardContext, moves, color);
    }

    public static boolean isKingInCheck(BoardContext boardContext, PieceColor color) throws BoardException {
        return MoveEvaluator.isKingInCheck(boardContext.getCopyOfBoard(),
                boardContext.getLastMove() != null ? boardContext.getLastMove().getMove() : null,
                BoardScanner.getCurrentSinglePiecePosition(boardContext.getCopyOfBoard(), PieceType.KING, color),
                color);
    }

    public static boolean isPlayerCheckMate(BoardContext game, PieceColor color) throws BoardException {
        return isKingInCheck(game, color) && getAllLegalMovesByPieceColor(game, color).isEmpty();
    }
}