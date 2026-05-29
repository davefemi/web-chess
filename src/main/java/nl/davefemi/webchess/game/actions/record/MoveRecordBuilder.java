package nl.davefemi.webchess.game.actions.record;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.actions.*;
import nl.davefemi.webchess.game.board.*;

public class MoveRecordBuilder {
    public static MoveRecord getMoveRecord(Board board, Move move, PieceColor color, Piece capturedPiece) throws BoardException {
        if (move instanceof CastlingMove m){
            return getCastlingMoveRecord(board, m, color);
        }
        if (move instanceof PromotionMove m){
            return getPromotionMoveRecord(board, m, color, capturedPiece);
        }
        if (move instanceof EnPassantMove m)
            move = new SingleMove(m.from(), m.to());
        return getSingleMoveRecord(board, (SingleMove) move, color, capturedPiece);
    }

    private static SingleMoveRecord getSingleMoveRecord(Board board,
                                                        SingleMove move, PieceColor color, Piece capturedPiece) throws BoardException {
        PieceType capPiece = null;
        int capPieceID = 0;
        if (capturedPiece != null){
            capPiece = capturedPiece.getType();
            capPieceID = capturedPiece.getId();
        }
        return new SingleMoveRecord(
                move,
                color,
                board.getPieceAt(move.to()).getType(),
                board.getPieceAt(move.to()).getId(),
                capPiece,
                capPieceID);
    }

    private static CastlingMoveRecord getCastlingMoveRecord (Board board,
                                                             CastlingMove move, PieceColor color) throws BoardException {
        return new CastlingMoveRecord(
                move,
                color,
                board.getPieceAt(move.moveKing().to()).getId(),
                board.getPieceAt(move.moveRook().to()).getId());
    }

    private static PromotionMoveRecord getPromotionMoveRecord(Board board,
                                                              PromotionMove move, PieceColor color, Piece capturedPiece) throws BoardException {
        PieceType capPiece = null;
        int capPieceID = 0;
        if (capturedPiece != null){
            capPiece = capturedPiece.getType();
            capPieceID = capturedPiece.getId();
        }
        return new PromotionMoveRecord(
                move,
                color,
                capPiece,
                capPieceID,
                board.getPieceAt(move.move().to()).getType(),
                board.getPieceAt(move.move().to()).getId());
    }

}