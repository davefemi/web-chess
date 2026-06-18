package nl.davefemi.chess.play.model.actions;

import nl.davefemi.chess.play.model.game.Color;
import nl.davefemi.chess.play.model.actions.move.Move;

public interface MoveRecord {

    Move getMove();
    Color playerColor();
}
