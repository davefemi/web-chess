package nl.davefemi.chess.gameplay.model.action.record;

import nl.davefemi.chess.gameplay.model.game.Color;
import nl.davefemi.chess.gameplay.model.action.move.Move;

public interface MoveRecord {

    Move getMove();
    Color playerColor();
}
