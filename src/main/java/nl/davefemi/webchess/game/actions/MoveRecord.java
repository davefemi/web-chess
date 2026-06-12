package nl.davefemi.webchess.game.actions;

import nl.davefemi.webchess.game.actions.move.Move;
import nl.davefemi.webchess.game.Color;

public interface MoveRecord {

    Move getMove();
    Color playerColor();
}
