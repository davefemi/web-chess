package nl.davefemi.webchess.game.actions.record;

import nl.davefemi.webchess.game.actions.move.CastlingMove;
import nl.davefemi.webchess.game.actions.MoveRecord;
import nl.davefemi.webchess.game.Color;

public record CastlingMoveRecord(CastlingMove move,
                                 Color playerColor,
                                 int kingId,
                                 int rookId) implements MoveRecord {

    @Override
    public CastlingMove getMove(){
        return move;
    }
}
