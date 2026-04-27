package nl.davefemi.webchess.game.utility;

import nl.davefemi.webchess.game.record.CastlingMoveRecord;
import nl.davefemi.webchess.game.record.MoveRecord;
import nl.davefemi.webchess.game.record.PromotionMoveRecord;
import nl.davefemi.webchess.game.record.SingleMoveRecord;
import nl.davefemi.webchess.game.board.Board;
import nl.davefemi.webchess.game.actions.CastlingMove;
import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.Piece;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.board.PieceColor;

public class MoveRecordBuilder {
    public static MoveRecord getMoveRecord(Board board, Move move, PieceColor color, Piece capturedPiece){
        if (move instanceof CastlingMove m){
            return getCastlingMoveRecord(board, m, color);
        }
        if (move instanceof PromotionMove m){
            return getPromotionMoveRecord(board, m, color);
        }
        return getSingleMoveRecord(board, (SingleMove) move, color, capturedPiece);
    }

    private static SingleMoveRecord getSingleMoveRecord(Board board,
                                                        SingleMove move, PieceColor color, Piece capturedPiece){
        PieceType capPiece = null;
        int capPieceID =0;
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
                                                             CastlingMove move, PieceColor color){
        return new CastlingMoveRecord(
                move,
                color,
                board.getPieceAt(move.moveKing().to()).getId(),
                board.getPieceAt(move.moveRook().to()).getId());
    }

    private static PromotionMoveRecord getPromotionMoveRecord(Board board,
                                                              PromotionMove move, PieceColor color){
        return new PromotionMoveRecord(
                move,
                color,
                board.getPieceAt(move.move().to()).getType(),
                board.getPieceAt(move.move().to()).getId());
    }

}
