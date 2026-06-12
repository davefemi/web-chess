package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.GameStatus;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.game.actions.move.*;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.actions.MoveRecord;
import java.util.ArrayList;
import java.util.List;

import static nl.davefemi.webchess.game.Color.*;
import static nl.davefemi.webchess.game.board.PieceType.PAWN;

public final class RuleEngine {

    private RuleEngine(){
        throw new AssertionError("This class cannot be instantiated");
    }


    public static GameBoardContext applyMove(GameBoardContext boardContext, List<MoveRecord> moveHistory,
                                             Color pieceColor, Move move) throws BoardException, GameException, MoveException {
        if (!isMoveAllowed(boardContext, moveHistory, pieceColor, move)){
            if (isCheckMate(boardContext, pieceColor))
                throw new GameException(boardContext.getSideToMove() + " is check mate");
            if (isCheck(boardContext, pieceColor))
                throw new MoveException(boardContext.getSideToMove() + " is in check");
            throw new MoveException("Illegal move: " + move.toString());
        }
        return boardContext.applyValidatedMove(move);
    }

    public static List<Move> getAllLegalMovesByColor(GameBoardContext boardContext,
                                                     Color playerToMove) throws BoardException {
        List<Move> moves = new ArrayList<>();
        Board board = boardContext.getCopyOfBoard();
        Move lastMove = boardContext.getLastMove() != null ? boardContext.getLastMove().getMove() : null;
        moves.addAll(SinglePseudoMoveGenerator.generateMoves(board, playerToMove));
        moves.addAll(CastlingPseudoMoveGenerator.generateMoves(board, playerToMove));
        moves.addAll(EnPassantPseudoMoveGenerator.generateMoves(board, lastMove, playerToMove));
        return MoveEvaluator.filterCheckAfterMove(boardContext, moves, playerToMove);
    }

    public static boolean isCheck(GameBoardContext boardContext, Color color) throws BoardException {
        return MoveEvaluator.detectAttackOnKing(boardContext, color);
    }

    public static boolean isCheckMate(GameBoardContext boardContext, Color color) throws BoardException {
        return MoveEvaluator.detectAttackOnKing(boardContext, color)
                && getAllLegalMovesByColor(boardContext, color).isEmpty();
    }

    public static boolean isMoveAllowed
            (GameBoardContext boardContext, List<MoveRecord> moveHistory, Color color, Move move)
            throws BoardException, MoveException {
        if (move instanceof CastlingMove m)
            if (!(MoveEvaluator.isCastlingMoveLegal(boardContext.getCopyOfBoard(),
                    boardContext.getOriginalRooks(), moveHistory, m)))
                throw new MoveException("Castling is not allowed");
        if (move instanceof SingleMove m) {
            Piece p = boardContext.getCopyOfBoard().getPieceAt(m.from());
            if (p != null && p.type() == PAWN)
                if ((p.color() == WHITE && m.from().rank() == 6 ||
                        p.color() == BLACK && m.from().rank() == 1)) {
                    throw new MoveException("Single move not allowed here, use promotion move");
                }
        }
        if (move instanceof PromotionMove m) {
            if (!MoveEvaluator.isPromotionMoveLegal(boardContext.getCopyOfBoard(), m))
                throw new MoveException("Promotion not allowed");
            move = m.move();
        }
        return getAllLegalMovesByColor(boardContext, color).contains(move);
    }

    public static GameStatus evaluateStatus(GameBoardContext boardContext) throws BoardException {
        if (getAllLegalMovesByColor(boardContext, boardContext.getSideToMove()).isEmpty()) {
            if (MoveEvaluator.detectAttackOnKing(boardContext, boardContext.getSideToMove()))
                return GameStatus.checkmate(Color.getOpponent(boardContext.getSideToMove()));
            return GameStatus.stalemate();
        }
        return GameStatus.active();
    }
}