package nl.davefemi.webchess.game.actions;

import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.game.actions.move.Move;

public interface MoveRecord {

    Move getMove();
    Color playerColor();
}
