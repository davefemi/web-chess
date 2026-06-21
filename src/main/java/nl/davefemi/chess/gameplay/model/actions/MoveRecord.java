package nl.davefemi.chess.gameplay.model.actions;

import nl.davefemi.chess.gameplay.model.game.Color;
import nl.davefemi.chess.gameplay.model.actions.move.Move;

public interface MoveRecord {

    Move getMove();
    Color playerColor();
}
