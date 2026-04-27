package nl.davefemi.webchess.game.record;

import nl.davefemi.webchess.game.actions.CastlingMove;
import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.board.PieceColor;

public record CastlingMoveRecord(CastlingMove move,
                                 PieceColor player_color,
                                 int kingId,
                                 int rookId) implements MoveRecord{

    @Override
    public CastlingMove getMove(){
        return move;
    }
}
