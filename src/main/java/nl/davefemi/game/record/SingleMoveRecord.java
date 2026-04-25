package nl.davefemi.game.record;

import nl.davefemi.game.actions.SingleMove;
import nl.davefemi.game.board.PieceType;
import nl.davefemi.game.board.PieceColor;

public record SingleMoveRecord(SingleMove move,
                               PieceColor color,
                               PieceType movedPiece,
                               int movedPieceId,
                               PieceType capturedPiece,
                               int capturedPieceId)
        implements MoveRecord{

}
