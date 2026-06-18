package nl.davefemi.chess.play.model.actions.record;

import nl.davefemi.chess.play.model.actions.move.CastlingMove;
import nl.davefemi.chess.play.model.actions.MoveRecord;
import nl.davefemi.chess.play.model.game.Color;

public record CastlingMoveRecord(CastlingMove move,
                                 Color playerColor,
                                 int kingId,
                                 int rookId) implements MoveRecord {

    @Override
    public CastlingMove getMove(){
        return move;
    }
}
