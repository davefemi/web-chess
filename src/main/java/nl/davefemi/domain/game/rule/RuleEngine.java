package nl.davefemi.domain.game.rule;

import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.board.BoardScanner;
import nl.davefemi.domain.game.Game;
import nl.davefemi.domain.game.actions.move.CastlingMove;
import nl.davefemi.domain.game.actions.move.Move;
import nl.davefemi.domain.game.actions.move.PromotionMove;
import nl.davefemi.domain.game.utility.CastlingMoveGenerator;
import nl.davefemi.domain.game.utility.MoveGenerator;
import nl.davefemi.domain.board.PieceType;
import nl.davefemi.domain.board.PlayerColor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.MoveException;
import java.util.*;

public final class RuleEngine {

    private RuleEngine(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static boolean isMoveAllowed(Game game, PlayerColor color, Move move) throws BoardException, MoveException, GameException {
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

    public static List<Move> getLegalMoves(Game game, PlayerColor color) throws BoardException {
        List<Move> moves = new ArrayList<>();
        Board board = game.getCopyOfBoard();
        moves.addAll(MoveGenerator.generateMoves(board, color, true));
        moves.addAll(CastlingMoveGenerator.generateCastlingMoves(board, color, true));
        return moves;
    }

    public static boolean isCheck(Game game, PlayerColor color) throws BoardException {
        return MoveEvaluator.isKingCheck(game.getCopyOfBoard(),
                BoardScanner.getCurrentSinglePosition(game.getCopyOfBoard(), PieceType.KING, color),
                BoardScanner.getEnemyColor(color));

    }

    public static boolean isCheckMate(Game game, PlayerColor color) throws BoardException {
        return isCheck(game, color) && getLegalMoves(game, color).isEmpty();
    }

}