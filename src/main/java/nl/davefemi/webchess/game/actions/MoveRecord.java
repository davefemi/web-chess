package nl.davefemi.webchess.game.actions;

import nl.davefemi.webchess.game.actions.move.Move;
import nl.davefemi.webchess.game.board.PieceColor;

public interface MoveRecord {

    Move getMove();
    PieceColor playerColor();
}
