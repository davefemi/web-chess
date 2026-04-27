package nl.davefemi.webchess.game.record;

import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.board.PieceColor;

public record SingleMoveRecord(SingleMove move,
                               PieceColor color,
                               PieceType movedPiece,
                               int movedPieceId,
                               PieceType capturedPiece,
                               int capturedPieceId)
        implements MoveRecord{

    @Override
    public SingleMove getMove(){
        return move;
    }

}
