package nl.davefemi.chess.gameplay.model.action.record;

import nl.davefemi.chess.gameplay.model.action.move.PromotionMove;
import nl.davefemi.chess.gameplay.model.board.PieceType;
import nl.davefemi.chess.gameplay.model.game.Color;

public record PromotionMoveRecord(PromotionMove move,
                                  Color playerColor,
                                  int newPieceId,
                                  PieceType capturedPiece,
                                  int capturedPieceId
                                  )
        implements MoveRecord {
    @Override
    public PromotionMove getMove(){
        return move;
    }
}
