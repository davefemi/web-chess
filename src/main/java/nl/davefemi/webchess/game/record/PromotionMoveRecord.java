package nl.davefemi.webchess.game.record;

import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.board.PieceColor;

public record PromotionMoveRecord(PromotionMove move,
                                  PieceColor playerColor,
                                  PieceType newPiece,
                                  int pieceId)
        implements MoveRecord{
    @Override
    public PromotionMove getMove(){
        return move;
    }
}
