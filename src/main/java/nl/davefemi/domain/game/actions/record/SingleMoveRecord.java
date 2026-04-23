package nl.davefemi.domain.game.actions.record;

import nl.davefemi.domain.game.actions.move.SingleMove;
import nl.davefemi.domain.board.PieceType;
import nl.davefemi.domain.board.PlayerColor;

public record SingleMoveRecord(SingleMove move,
                               PlayerColor color,
                               PieceType movedPiece,
                               int movedPieceId,
                               PieceType capturedPiece,
                               int capturedPieceId)
        implements MoveRecord{

}
