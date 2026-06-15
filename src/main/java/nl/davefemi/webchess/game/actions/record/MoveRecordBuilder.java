package nl.davefemi.webchess.game.actions.record;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.game.actions.*;
import nl.davefemi.webchess.game.actions.move.*;
import nl.davefemi.webchess.game.board.*;

public class MoveRecordBuilder {
    public static MoveRecord getMoveRecord(Board board, Move move, Color color, Piece capturedPiece)
            throws BoardException {
        if (move instanceof CastlingMove m){
            return getCastlingMoveRecord(board, m, color);
        }
        if (move instanceof PromotionMove m){
            return getPromotionMoveRecord(board, m, color, capturedPiece);
        }
        if (move instanceof EnPassantMove m) {
            return getEnPassantMoveRecord(board, m, color, capturedPiece);
        }
        return getSingleMoveRecord(board, (SingleMove) move, color, capturedPiece);
    }

    private static SingleMoveRecord getSingleMoveRecord(Board board, SingleMove move, Color color,
                                                        Piece capturedPiece) throws BoardException {
        PieceType pieceType = null;
        int capPieceId = 0;
        if (capturedPiece != null){
            pieceType = capturedPiece.type();
            capPieceId = capturedPiece.id();
        }
        return new SingleMoveRecord(
                move,
                color,
                board.getPieceAt(move.to()).type(),
                board.getPieceAt(move.to()).id(),
                pieceType,
                capPieceId);
    }

    private static EnPassantMoveRecord getEnPassantMoveRecord(Board board, EnPassantMove move, Color color,
                                                        Piece capturedPiece) throws BoardException {
        PieceType pieceType = null;
        int capPieceId = 0;
        if (capturedPiece != null){
            pieceType = capturedPiece.type();
            capPieceId = capturedPiece.id();
        }
        return new EnPassantMoveRecord(
                move,
                color,
                board.getPieceAt(move.to()).type(),
                board.getPieceAt(move.to()).id(),
                pieceType,
                capPieceId);
    }

    private static CastlingMoveRecord getCastlingMoveRecord (Board board,
                                                             CastlingMove move, Color color) throws BoardException {
        return new CastlingMoveRecord(
                move,
                color,
                board.getPieceAt(move.moveKing().to()).id(),
                board.getPieceAt(move.moveRook().to()).id());
    }

    private static PromotionMoveRecord getPromotionMoveRecord(Board board, PromotionMove move, Color color,
                                                              Piece capturedPiece) throws BoardException {
        PieceType capPiece = null;
        int capPieceID = 0;
        if (capturedPiece != null){
            capPiece = capturedPiece.type();
            capPieceID = capturedPiece.id();
        }
        return new PromotionMoveRecord(
                move,
                color,
                board.getPieceAt(move.move().to()).id(),
                capPiece,
                capPieceID);
    }

}