package nl.davefemi.game.record;

import nl.davefemi.game.actions.PromotionMove;
import nl.davefemi.game.board.PieceType;
import nl.davefemi.game.board.PieceColor;

public record PromotionMoveRecord(PromotionMove move,
                                  PieceColor playerColor,
                                  PieceType newPiece,
                                  int pieceId)
        implements MoveRecord{
}
