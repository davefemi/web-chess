package nl.davefemi.game.record;

import nl.davefemi.game.actions.CastlingMove;
import nl.davefemi.game.board.PieceColor;

public record CastlingMoveRecord(CastlingMove move,
                                 PieceColor player_color,
                                 int kingId,
                                 int rookId) implements MoveRecord{
}
