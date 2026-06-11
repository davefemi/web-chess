package nl.davefemi.webchess.game.actions.record;

import nl.davefemi.webchess.game.actions.MoveRecord;
import nl.davefemi.webchess.game.actions.move.EnPassantMove;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.board.PieceType;

public record EnPassantMoveRecord(EnPassantMove move,
                                  PieceColor playerColor,
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
