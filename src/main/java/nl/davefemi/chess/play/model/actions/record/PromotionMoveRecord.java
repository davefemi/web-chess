package nl.davefemi.chess.play.model.actions.record;

import nl.davefemi.chess.play.model.actions.MoveRecord;
import nl.davefemi.chess.play.model.actions.move.PromotionMove;
import nl.davefemi.chess.play.model.board.PieceType;
import nl.davefemi.chess.play.model.game.Color;

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
