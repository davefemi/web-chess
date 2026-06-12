package nl.davefemi.webchess.game.actions.record;

import nl.davefemi.webchess.game.actions.MoveRecord;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.Color;

public record SingleMoveRecord(SingleMove move,
                               Color playerColor,
                               PieceType movedPiece,
                               int movedPieceId,
                               PieceType capturedPiece,
                               int capturedPieceId)
        implements MoveRecord {

    @Override
    public SingleMove getMove(){
        return move;
    }

}
