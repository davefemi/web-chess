package nl.davefemi.domain.game.rule;

import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.board.BoardScanner;
import nl.davefemi.domain.game.Game;
import nl.davefemi.domain.game.move.Move;
import nl.davefemi.domain.game.move.PromotionMove;
import nl.davefemi.domain.game.utility.CastlingMoveGenerator;
import nl.davefemi.domain.game.utility.MoveGenerator;
import nl.davefemi.domain.piece.PieceType;
import nl.davefemi.domain.piece.PlayerColor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.MoveException;
import java.util.*;

public final class RuleEngine {

    private RuleEngine(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static boolean isMoveAllowed(Game game, PlayerColor color, Move move) {
        if (PromotionVerifier.checkForPromotion(game.getCopyOfBoard())) {
            if (move instanceof PromotionMove)
                return PromotionVerifier.isPromotionLegal(game.getCopyOfBoard(), (PromotionMove) move);
            throw new BoardException("A pawn is up for promotion");
        }
        if (!(color == game.getTurn()))
            throw new MoveException("It is not " + color +"'s turn yet");
        return getLegalMoves(game, color).contains(move);
    }

    public static boolean checkForPromotion(Game game){
        return PromotionVerifier.checkForPromotion(game.getCopyOfBoard());
    }

    public static List<Move> getLegalMoves(Game game, PlayerColor color){
        if (BoardScanner.getCurrentPosition(game.getCopyOfBoard(), PieceType.KING, PlayerColor.BLACK) == null ||
                BoardScanner.getCurrentPosition(game.getCopyOfBoard(), PieceType.KING, PlayerColor.WHITE )== null)
            throw new GameException("Game cannot continue without a both kings");
        List<Move> moves = new ArrayList<>();
        Board board = game.getCopyOfBoard();
        moves.addAll(MoveGenerator.generateMoves(board, color, true));
        moves.addAll(CastlingMoveGenerator.generateCastlingMoves(board, color, true));
        return moves;
    }

    public static boolean isCheck(Game game, PlayerColor color){
        return MoveEvaluator.isKingCheck(game.getCopyOfBoard(),
                BoardScanner.getCurrentPosition(game.getCopyOfBoard(), PieceType.KING, color),
                BoardScanner.getEnemyColor(color));

    }

    public static boolean isCheckMate(Game game, PlayerColor color){
        return isCheck(game, color) && getLegalMoves(game, color).isEmpty();
    }

}