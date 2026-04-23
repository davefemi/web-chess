package nl.davefemi.domain.game.actions.record;

import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.game.actions.move.CastlingMove;
import nl.davefemi.domain.game.actions.move.Move;
import nl.davefemi.domain.game.actions.move.PromotionMove;
import nl.davefemi.domain.game.actions.move.SingleMove;
import nl.davefemi.domain.board.Piece;
import nl.davefemi.domain.board.PieceType;
import nl.davefemi.domain.board.PlayerColor;

public class MoveRecordBuilder {
    public static MoveRecord getMoveRecord(Board board, Move move, PlayerColor color, Piece capturedPiece){
        if (move instanceof CastlingMove m){
            return getCastlingMoveRecord(board, m, color);
        }
        if (move instanceof PromotionMove m){
            return getPromotionMoveRecord(board, m, color);
        }
        return getSingleMoveRecord(board, (SingleMove) move, color, capturedPiece);
    }

    private static SingleMoveRecord getSingleMoveRecord(Board board,
                                                        SingleMove move, PlayerColor color, Piece capturedPiece){
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
                                                             CastlingMove move, PlayerColor color){
        return new CastlingMoveRecord(
                move,
                color,
                board.getPieceAt(move.moveKing().to()).getId(),
                board.getPieceAt(move.moveRook().to()).getId());
    }

    private static PromotionMoveRecord getPromotionMoveRecord(Board board,
                                                              PromotionMove move, PlayerColor color){
        return new PromotionMoveRecord(
                move,
                color,
                board.getPieceAt(move.position()).getType(),
                board.getPieceAt(move.position()).getId());
    }

}
