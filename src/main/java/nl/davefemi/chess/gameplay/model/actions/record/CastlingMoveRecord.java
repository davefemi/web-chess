package nl.davefemi.chess.gameplay.model.actions.record;

import nl.davefemi.chess.gameplay.model.actions.move.CastlingMove;
import nl.davefemi.chess.gameplay.model.actions.MoveRecord;
import nl.davefemi.chess.gameplay.model.game.Color;

public record CastlingMoveRecord(CastlingMove move,
                                 Color playerColor,
                                 int kingId,
                                 int rookId) implements MoveRecord {

    @Override
    public CastlingMove getMove(){
        return move;
    }
}
