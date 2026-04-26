package nl.davefemi.game.rule;

import nl.davefemi.game.board.Board;
import nl.davefemi.game.board.BoardScanner;
import nl.davefemi.game.Game;
import nl.davefemi.game.actions.CastlingMove;
import nl.davefemi.game.actions.Move;
import nl.davefemi.game.actions.PromotionMove;
import nl.davefemi.game.utility.CastlingMoveGenerator;
import nl.davefemi.game.utility.MoveGenerator;
import nl.davefemi.game.board.PieceType;
import nl.davefemi.game.board.PieceColor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.MoveException;
import java.util.*;

public final class RuleEngine {

    private RuleEngine(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static boolean isMoveAllowed(Game game, PieceColor pieceColor, Move move) throws BoardException, MoveException, GameException {
        if (!(pieceColor == game.getPlayerTurn()))
            throw new MoveException("It is not " + pieceColor +"'s turn yet");
        if (move instanceof CastlingMove m)
            if (!(MoveEvaluator.isCastlingMoveLegal(game.getCopyOfBoard(), game.getOriginalRooks(), game.getMoveHistory(), m)))
                throw new MoveException("Castling is not allowed");
        if (move instanceof PromotionMove m) {
            if (!PromotionMoveEvaluator.isPromotionMoveLegal(game.getCopyOfBoard(), m))
                throw new MoveException("Promotion not allowed");
            move = m.move();
        }
        return getAllLegalMovesByPieceColor(game, pieceColor).contains(move);
    }

    public static List<Move> getAllLegalMovesByPieceColor(Game game, PieceColor color) throws BoardException {
        List<Move> moves = new ArrayList<>();
        Board board = game.getCopyOfBoard();
        moves.addAll(MoveGenerator.generateMoves(board, color, true));
        moves.addAll(CastlingMoveGenerator.generateCastlingMoves(board, color, true));
        return moves;
    }

    public static boolean isCheck(Game game, PieceColor color) throws BoardException {
        return MoveEvaluator.isKingCheck(game.getCopyOfBoard(),
                BoardScanner.getCurrentSinglePiecePosition(game.getCopyOfBoard(), PieceType.KING, color),
                PieceColor.getOpponent(color));
    }

    public static boolean isCheckMate(Game game, PieceColor color) throws BoardException {
        return isCheck(game, color) && getAllLegalMovesByPieceColor(game, color).isEmpty();
    }

}