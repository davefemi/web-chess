package nl.davidfemi.domain.game.rules;

import nl.davidfemi.domain.board.BoardScanner;
import nl.davidfemi.domain.game.utility.CastlingMove;
import nl.davidfemi.domain.game.Game;
import nl.davidfemi.domain.game.utility.Move;
import nl.davidfemi.domain.game.utility.CastlingMoveGenerator;
import nl.davidfemi.domain.game.utility.MoveGenerator;
import nl.davidfemi.domain.pieces.PieceType;
import nl.davidfemi.domain.pieces.PlayerColor;
import nl.davidfemi.exception.BoardException;
import nl.davidfemi.exception.MoveException;
import java.util.*;

public final class RuleEngine {

    private RuleEngine(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static boolean isMoveAllowed(Game game, PlayerColor color, Move move) {
        if (!(color == game.getTurn()))
            throw new MoveException("It is not " + color +"'s turn yet");
        if (PromotionVerifier.checkForPromotion(game.getCopyOfBoard()))
            throw new BoardException("A pawn is up for promotion");
        return (getLegalMoves(game, color).contains(move));
    }

    public static boolean isMoveAllowed(Game game, PlayerColor color, CastlingMove move){
        if (!(color == game.getTurn()))
            throw new MoveException("It is not " + color +"'s turn yet");
        return getLegalCastlingMoves(game, color).contains(move);
    }

    public static List<Move> getLegalMoves(Game game, PlayerColor color){
        if (!(color == game.getTurn()))
            throw new MoveException("It is not " + color +"'s turn yet");
        if (PromotionVerifier.checkForPromotion(game.getCopyOfBoard()))
            throw new BoardException("A pawn is up for promotion");
        return MoveGenerator.generateMoves(game.getCopyOfBoard(), game.getTurn(), true);
    }

    public static List<CastlingMove> getLegalCastlingMoves(Game game, PlayerColor color){
        if (!(color == game.getTurn()))
            throw new MoveException("It is not " + color +"'s turn yet");
        if (PromotionVerifier.checkForPromotion(game.getCopyOfBoard()))
            throw new BoardException("A pawn is up for promotion");
        return CastlingMoveGenerator.generateCastlingMoves(game.getCopyOfBoard(), game.getTurn());
    }

    public static boolean isCheck(Game game, PlayerColor color){
        if (game.getTurn() == color){
            return MoveEvaluator.isKingCheck(game.getCopyOfBoard(),
                    BoardScanner.getPosition(game.getCopyOfBoard(), PieceType.KING, color),
                    BoardScanner.getEnemyColor(color));
        }
        return false;
    }

    public static boolean isCheckMate(Game game, PlayerColor color){
        return isCheck(game, color) && getLegalMoves(game, color).isEmpty() && getLegalCastlingMoves(game, color).isEmpty();
    }

}