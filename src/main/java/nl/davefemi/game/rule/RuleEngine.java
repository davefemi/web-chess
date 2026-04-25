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

    public static boolean isMoveAllowed(Game game, PieceColor color, Move move) throws BoardException, MoveException, GameException {
        if (PromotionMoveEvaluator.checkForPromotion(game.getCopyOfBoard())) {
            if (move instanceof PromotionMove m)
                return PromotionMoveEvaluator.isPromotionLegal(game.getCopyOfBoard(), m);
            throw new BoardException("A pawn is up for promotion");
        }
        if (!(color == game.getPlayerTurn()))
            throw new MoveException("It is not " + color +"'s turn yet");
        if (move instanceof CastlingMove m)
            if (!(MoveEvaluator.isCastlingLegal(game.getCopyOfBoard(), game.getMoveHistory(), m)))
                throw new MoveException("Castling is not allowed");
        return getLegalMoves(game, color).contains(move);
    }

    public static boolean checkForPromotion(Game game){
        return PromotionMoveEvaluator.checkForPromotion(game.getCopyOfBoard());
    }

    public static List<Move> getLegalMoves(Game game, PieceColor color) throws BoardException {
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
        return isCheck(game, color) && getLegalMoves(game, color).isEmpty();
    }

}