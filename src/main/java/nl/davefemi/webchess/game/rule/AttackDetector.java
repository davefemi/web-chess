package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.move.Move;
import nl.davefemi.webchess.game.board.*;

public final class AttackDetector {
    private AttackDetector(){
        throw new AssertionError("This class cannot be instantiated");
    }

    static boolean isPositionExposed(Board board, Move lastMove, Square position, PieceColor playingColor) throws BoardException {

        return false;
    }

}
