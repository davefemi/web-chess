package nl.davefemi.chess.play.model.actions.record;

import nl.davefemi.chess.play.model.actions.MoveRecord;
import nl.davefemi.chess.play.model.actions.move.SingleMove;
import nl.davefemi.chess.play.model.board.PieceType;
import nl.davefemi.chess.play.model.game.Color;

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
