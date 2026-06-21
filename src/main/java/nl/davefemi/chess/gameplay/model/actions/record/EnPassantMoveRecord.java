package nl.davefemi.chess.gameplay.model.actions.record;

import nl.davefemi.chess.gameplay.model.actions.MoveRecord;
import nl.davefemi.chess.gameplay.model.actions.move.EnPassantMove;
import nl.davefemi.chess.gameplay.model.game.Color;
import nl.davefemi.chess.gameplay.model.board.PieceType;

public record EnPassantMoveRecord(EnPassantMove move,
                                  Color playerColor,
                                  PieceType movedPiece,
                                  int movedPieceId,
                                  PieceType capturedPiece,
                                  int capturedPieceId)
        implements MoveRecord {

    @Override
    public EnPassantMove getMove(){
        return move;
    }

}
