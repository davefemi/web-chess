package nl.davefemi.chess.play.model.actions.record;

import nl.davefemi.chess.play.model.actions.MoveRecord;
import nl.davefemi.chess.play.model.actions.move.EnPassantMove;
import nl.davefemi.chess.play.model.game.Color;
import nl.davefemi.chess.play.model.board.PieceType;

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
