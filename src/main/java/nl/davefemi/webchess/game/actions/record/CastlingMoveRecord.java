package nl.davefemi.webchess.game.actions.record;

import nl.davefemi.webchess.game.actions.move.CastlingMove;
import nl.davefemi.webchess.game.actions.MoveRecord;
import nl.davefemi.webchess.game.board.PieceColor;

public record CastlingMoveRecord(CastlingMove move,
                                 PieceColor player_color,
                                 int kingId,
                                 int rookId) implements MoveRecord {

    @Override
    public CastlingMove getMove(){
        return move;
    }
}
