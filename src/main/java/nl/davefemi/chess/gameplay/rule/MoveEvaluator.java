package nl.davefemi.chess.gameplay.rule;

import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.MoveException;
import nl.davefemi.chess.exception.TypeException;
import nl.davefemi.chess.gameplay.model.action.move.CastlingMove;
import nl.davefemi.chess.gameplay.model.action.move.PromotionMove;
import nl.davefemi.chess.gameplay.model.action.move.SingleMove;
import nl.davefemi.chess.gameplay.model.board.*;
import nl.davefemi.chess.gameplay.model.game.Color;
import nl.davefemi.chess.gameplay.model.action.move.Move;
import nl.davefemi.chess.gameplay.model.action.record.CastlingMoveRecord;
import nl.davefemi.chess.gameplay.model.action.record.MoveRecord;
import nl.davefemi.chess.gameplay.model.action.record.SingleMoveRecord;
import java.util.ArrayList;
import java.util.List;

import static nl.davefemi.chess.gameplay.model.board.PieceType.*;
import static nl.davefemi.chess.gameplay.model.game.Color.*;

public final class MoveEvaluator {

    private MoveEvaluator(){
        throw new AssertionError("This class cannot be instantiated");
    }

    static boolean detectAttackOnKing(GameBoardContext boardContext, Color color) throws BoardException {
        Square kingPosition = boardContext.getCopyOfBoard().getPositionsByTypeAndColor(KING, color).getFirst();
        return AttackDetector.detectAttack(boardContext.getCopyOfBoard(), kingPosition, color);
    }

    static List<Move> filterCheckAfterMove(GameBoardContext boardContext, List<Move> pseudoMoves,
                                           Color player) throws BoardException {
        List<Move> legalMoves = new ArrayList<>();
        for (Move move : pseudoMoves) {
            GameBoardContext boardAfterMove = boardContext.applyValidatedMove(move);
            if (detectAttackOnKing(boardAfterMove, player)) {
                continue;
            }
            legalMoves.add(move);
        }
        return legalMoves;
    }

    static boolean isCastlingMoveLegal(Board board, List<Integer> originalRooks, List<MoveRecord> moveHistory,
                                       CastlingMove move) throws MoveException, BoardException {
        Piece king = board.getPieceAt(move.moveKing().from());
        Piece rook = board.getPieceAt(move.moveRook().from());
        if (king != null && king.type() == KING &&
                rook != null && rook.type() == ROOK){
            if (!originalRooks.contains(rook.id())) {
                throw new MoveException("Not allowed: this is not an original " + rook.type());
            }
            for (MoveRecord m : moveHistory){
                if (m instanceof CastlingMoveRecord cm){
                    if (cm.kingId() == king.id()){
                        throw new MoveException("Not allowed: " + king.type() + " already has castled");
                    }
                }
                if (m instanceof SingleMoveRecord sm){
                    if (rook.id() == sm.movedPieceId()) {
                        throw new MoveException("Not allowed: " + rook.type() + " has moved before");
                    }
                    if (king.id() == sm.movedPieceId()) {
                        throw new MoveException("Not allowed: " + king.type() + " has moved before");
                    }
                }
            }
            return true;
        }
        throw new MoveException("Invalid positions");
    }

    static boolean isPromotionMoveLegal(Board board, PromotionMove move)
            throws MoveException, BoardException {
        PieceType newPieceType = move.newPieceType();
        Square oldPawnPos = move.move().from();
        Piece piece = board.getPieceAt(oldPawnPos);
        if (piece == null){
            throw new MoveException("There is no piece at the position to be moved");
        }
        if (newPieceType == null) {
            throw new TypeException("For promotion you have to specify a valid replacement type");
        }
        Color color = board.getPieceAt(oldPawnPos).color();
        if (board.getPieceAt(oldPawnPos).type() != PAWN) {
            throw new TypeException("Piece to be replaced is not a pawn");
        }
        if (color == WHITE && oldPawnPos.rank() != 6 ||
                color == BLACK && oldPawnPos.rank() != 1) {
            throw new MoveException("This piece is not up for promotion");
        }
        if (newPieceType == PAWN || newPieceType == KING) {
            throw new TypeException("Replacement piece cannot be of type " + newPieceType);
        }
        return true;
    }

    static boolean isPawnMoveLegal(Board board, Square oldPos, Square newPos) throws BoardException {
        if (board.isPositionOccupied(newPos) &&
                board.getPieceAt(newPos).color() != board.getPieceAt(oldPos).color() &&
                oldPos.file() != newPos.file()) {
            return true;
        }
        return !board.isPositionOccupied(newPos) && oldPos.file() == newPos.file();
    }

    static boolean isEnpassantLegal(Board board, Move lastMoveInGame, Square oldPosition,
                                    Square newPosition, Color playingColor) throws BoardException {
        boolean newFileDifferenceOfOne = Math.abs(newPosition.file() - oldPosition.file()) == 1;
        boolean isPlayingColorWhite = playingColor == WHITE;
        int startingRankPlayingColor = isPlayingColorWhite? 4 : 3;
        int newRankPlayingColor = isPlayingColorWhite? 5 : 2;
        int startingRankOpponent = isPlayingColorWhite? 6 : 1;
        int newRankOpponent = isPlayingColorWhite? 4 : 3;
        if (newPosition.rank() == newRankPlayingColor && oldPosition.rank() == startingRankPlayingColor
                && newFileDifferenceOfOne && lastMoveInGame instanceof SingleMove(Square from, Square v)
                && v.rank() == newRankOpponent && from.rank() == startingRankOpponent
                && v.file() == newPosition.file()){
            Piece piece = board.getPieceAt(v);
            return piece.type() == PAWN && piece.color() != playingColor;
        }
        return false;
    }

    static boolean filterForBlockingCapturePositions(Board board, Square oldPos, Square newPos,
                                                     List<SingleMove> moves) throws BoardException {
        if (board.isPositionOccupied(newPos)) {
            if (board.getPieceAt(oldPos).color() == board.getPieceAt(newPos).color())
                return true;
            moves.add(new SingleMove(oldPos, newPos));
            return true;
        }
        return false;
    }
}