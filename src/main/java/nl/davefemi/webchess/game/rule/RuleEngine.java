package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.GameStatus;
import nl.davefemi.webchess.game.actions.move.CastlingMove;
import nl.davefemi.webchess.game.actions.move.Move;
import nl.davefemi.webchess.game.actions.move.PromotionMove;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.actions.MoveRecord;
import java.util.ArrayList;
import java.util.List;

public class RuleEngine {

    public static BoardContext applyLegalMove(BoardContext boardContext, List<MoveRecord> moveHistory, PieceColor pieceColor, Move move) throws BoardException, GameException, MoveException {
        if (!isMoveAllowed(boardContext, moveHistory, pieceColor, move)){
            if (isPlayerCheckMate(boardContext, pieceColor))
                throw new GameException(boardContext.getColorToMove() + " is check mate");
            if (isKingInCheck(boardContext, pieceColor))
                throw new MoveException(boardContext.getColorToMove() + " is in check");
            throw new MoveException("Illegal move");
        }
        return boardContext.applyMove(move);
    }

    public static List<Move> getAllLegalMovesByPieceColor(BoardContext boardContext, PieceColor playerToMove) throws BoardException {
        List<Move> moves = new ArrayList<>();
        Board board = boardContext.getCopyOfBoard();
        Move lastMove = boardContext.getLastMove() != null ? boardContext.getLastMove().getMove() : null;
        moves.addAll(PseudoSingleMoveGenerator.generateMoves(board, playerToMove));
        moves.addAll(PseudoCastlingMoveGenerator.generateMoves(board, playerToMove));
        moves.addAll(PseudoEnPassantMoveGenerator.generateMoves(board, lastMove, playerToMove));
        return MoveEvaluator.evaluateIfKingIsInCheckAfterMove(boardContext, moves, playerToMove);
    }

    public static boolean isKingInCheck(BoardContext boardContext, PieceColor playerToMove) throws BoardException {
        return MoveEvaluator.isKingInCheck(boardContext, playerToMove);
    }

    public static boolean isPlayerCheckMate(BoardContext boardContext, PieceColor playerToMove) throws BoardException {
        return isKingInCheck(boardContext, playerToMove) && getAllLegalMovesByPieceColor(boardContext, playerToMove).isEmpty();
    }

    public static boolean isMoveAllowed(BoardContext boardContext, List<MoveRecord> moveHistory, PieceColor pieceColor, Move move) throws BoardException, MoveException {
        if (!(pieceColor == boardContext.getColorToMove()))
            throw new MoveException("It is not " + pieceColor + "'s turn yet");
        if (move instanceof CastlingMove m)
            if (!(MoveEvaluator.isCastlingMoveLegal(boardContext.getCopyOfBoard(), boardContext.getOriginalRooks(), moveHistory, m)))
                throw new MoveException("Castling is not allowed");
        if (move instanceof SingleMove m) {
            Piece p = boardContext.getCopyOfBoard().getPieceAt(m.from());
            if (p != null && p.getType() == PieceType.PAWN)
                if ((p.getColor() == PieceColor.WHITE && m.from().rank() == 6 ||
                        p.getColor() == PieceColor.BLACK && m.from().rank() == 1)) {
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

    public static GameStatus evaluateStatus(BoardContext boardContext) throws BoardException {
        if (isPlayerCheckMate(boardContext, boardContext.getColorToMove()))
            return GameStatus.checkmate(PieceColor.getOpponent(boardContext.getColorToMove()));
        return GameStatus.active();
    }
}
