package nl.davefemi.chess.gameplay.model.action.record;

import nl.davefemi.chess.gameplay.model.action.move.CastlingMove;
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
