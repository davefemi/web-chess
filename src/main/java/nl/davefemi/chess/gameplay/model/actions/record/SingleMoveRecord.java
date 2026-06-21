package nl.davefemi.chess.gameplay.model.actions.record;

import nl.davefemi.chess.gameplay.model.actions.MoveRecord;
import nl.davefemi.chess.gameplay.model.actions.move.SingleMove;
import nl.davefemi.chess.gameplay.model.board.PieceType;
import nl.davefemi.chess.gameplay.model.game.Color;

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
