package nl.davefemi.domain.game.actions.record;

import nl.davefemi.domain.game.actions.move.CastlingMove;
import nl.davefemi.domain.board.PlayerColor;

public record CastlingMoveRecord(CastlingMove move,
                                 PlayerColor player_color,
                                 int kingId,
                                 int rookId) implements MoveRecord{
}
