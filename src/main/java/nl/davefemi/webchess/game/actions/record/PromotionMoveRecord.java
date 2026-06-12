package nl.davefemi.webchess.game.actions.record;

import nl.davefemi.webchess.game.actions.MoveRecord;
import nl.davefemi.webchess.game.actions.move.PromotionMove;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.Color;

public record PromotionMoveRecord(PromotionMove move,
                                  Color playerColor,
                                  PieceType capturedPiece,
                                  int capturedPieceId,
                                  PieceType newPiece,
                                  int pieceId)
        implements MoveRecord {
    @Override
    public PromotionMove getMove(){
        return move;
    }
}
