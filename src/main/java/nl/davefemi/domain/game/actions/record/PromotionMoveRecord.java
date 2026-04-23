package nl.davefemi.domain.game.actions.record;

import nl.davefemi.domain.game.actions.move.PromotionMove;
import nl.davefemi.domain.board.PieceType;
import nl.davefemi.domain.board.PlayerColor;

public record PromotionMoveRecord(PromotionMove move,
                                  PlayerColor playerColor,
                                  PieceType newPiece,
                                  int pieceId)
        implements MoveRecord{
}
