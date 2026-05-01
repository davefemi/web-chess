package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.exception.TypeException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.actions.*;
import nl.davefemi.webchess.game.board.Board;
import nl.davefemi.webchess.game.board.Position;
import nl.davefemi.webchess.game.record.CastlingMoveRecord;
import nl.davefemi.webchess.game.record.MoveRecord;
import nl.davefemi.webchess.game.record.SingleMoveRecord;
import nl.davefemi.webchess.game.board.Piece;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.MoveException;
import java.util.ArrayList;
import java.util.List;

public final class MoveEvaluator {

    private MoveEvaluator(){
        throw new AssertionError("This class cannot be instantiated");
    }

    static boolean isCastlingMoveLegal(Board board, List<Integer> originalRooks, List<MoveRecord> moveHistory, CastlingMove move) throws MoveException {
        Piece king = board.getPieceAt(move.moveKing().from());
        Piece rook = board.getPieceAt(move.moveRook().from());
        if (king != null && king.getType() == PieceType.KING &&
                rook != null && rook.getType() == PieceType.ROOK){
            if (!originalRooks.contains(rook.getId()))
                throw new MoveException("Not allowed: this is not an original " + rook.getType());
            for (MoveRecord m : moveHistory){
                if (m instanceof CastlingMoveRecord cm){
                    if (cm.kingId() == king.getId()){
                        throw new MoveException("Not allowed: " + king.getType() + " already has castled");
                    }
                }
                if (m instanceof SingleMoveRecord sm){
                    if (rook.getId() == sm.movedPieceId())
                        throw new MoveException("Not allowed: " + rook.getType() + " has moved before");
                    if (king.getId() == sm.movedPieceId())
                        throw new MoveException("Not allowed: " + king.getType() + " has moved before");
                }
            }
            return true;
        }
        throw new MoveException("Invalid positions");
    }

    static boolean isPromotionMoveLegal(Board board, PromotionMove move) throws MoveException {
        PieceType newPieceType = move.newPieceType();
        Position oldPawnPos = move.move().from();
        Piece piece = board.getPieceAt(oldPawnPos);
        if (piece == null){
            throw new MoveException("There is no piece at the position to be moved");
        }
        if (newPieceType == null)
            throw new TypeException("For promotion you have to specify a valid replacement type");
        PieceColor color = board.getPieceAt(oldPawnPos).getColor();
        if (board.getPieceAt(oldPawnPos).getType() != PieceType.PAWN)
            throw new TypeException("Piece to be replaced is not a pawn");
        if (color == PieceColor.WHITE && oldPawnPos.rank() != 7 ||
                color == PieceColor.BLACK && oldPawnPos.rank() != 2)
            throw new MoveException("This piece is not up for promotion");
        if (newPieceType == PieceType.PAWN || newPieceType == PieceType.KING)
            throw new TypeException("Replacement piece cannot be of type " + newPieceType.getLabel());
        return true;
    }

    static List<Move> evaluateIfKingIsInCheckAfterMove(Game game, List<Move> pseudoMoves, PieceColor player) throws BoardException {
        List<Move> legalMoves = new ArrayList<>();
        for (Move move : pseudoMoves) {
            Board boardAfterMove = PseudoSingleMoveGenerator.applyFictitiousMove(game.getCopyOfBoard(), move);
            Position kingOfPlayer = boardAfterMove.getPositionsByTypeAndColor(PieceType.KING, player).getFirst();
            if (isKingInCheck(boardAfterMove, move, kingOfPlayer, player))
                continue;
            legalMoves.add(move);
        }
        return legalMoves;
    }

    static boolean isKingInCheck(Board board, Move lastMove, Position kingPosition, PieceColor player) throws BoardException {
        for (Move opponentMove : getPseudoMoves(board,lastMove, PieceColor.getOpponent(player))) {
            Board boardAfterOpponentMove = PseudoSingleMoveGenerator.applyFictitiousMove(board, opponentMove);
            List<Position> boardPositions = boardAfterOpponentMove.getPositionsByTypeAndColor(PieceType.KING, PieceColor.getOpponent(player));
            if (boardPositions.isEmpty())
                continue;
            Position positionOpponentKing = boardPositions.getFirst();
            if (opponentMove instanceof SingleMove singleCounterMoveOpponent) {
                if (moveAttacksKing(singleCounterMoveOpponent, kingPosition) &&
                        kingNotInCheckAfterMakingMove(boardAfterOpponentMove, opponentMove, positionOpponentKing, player)) {
                    return true;
                }
                continue;
            }
            if (opponentMove instanceof EnPassantMove singleCounterMoveOpponent) {
                if (moveAttacksKing(singleCounterMoveOpponent, kingPosition) &&
                        kingNotInCheckAfterMakingMove(boardAfterOpponentMove, opponentMove, positionOpponentKing, player)) {
                    return true;
                }
                continue;
            }
            if (opponentMove instanceof CastlingMove castlingCounterMoveOpponent){
                if (moveAttacksKing(castlingCounterMoveOpponent, kingPosition) &&
                        kingNotInCheckAfterMakingMove(boardAfterOpponentMove, opponentMove, positionOpponentKing, player)){
                    return true;
                }
            }
        }
        return false;
    }

    static boolean filterForBlockingCapturePositions(Board board, Position oldPos, Position newPos, List<SingleMove> moves) {
        if (board.isBoardPositionOccupied(newPos)) {
            if (board.getPieceAt(oldPos).getColor() == board.getPieceAt(newPos).getColor())
                return true;
            moves.add(new SingleMove(oldPos, newPos));
            return true;
        }
        return false;
    }

    static boolean isPawnMoveLegal(Board board, Position oldPos, Position newPos) {
        if (board.isBoardPositionOccupied(newPos) &&
                board.getPieceAt(newPos).getColor() != board.getPieceAt(oldPos).getColor() &&
                oldPos.file() != newPos.file())
            return true;
        return !board.isBoardPositionOccupied(newPos) && oldPos.file() == newPos.file();
    }

    static boolean isEnpassantLegal(Board board, Move lastMove, Position oldPos, Position newPos, PieceColor color){
        boolean newFileDifferenceOfOne = newPos.file() - oldPos.file() == Math.abs(1);
        boolean white = color == PieceColor.WHITE;
        int startingRank = white ? 5 : 4;
        int newRank = white ? 6 : 3;
        int startingRankOpponent = white ? 7 : 2;
        int newRankOpponent = white ? 5 : 4;
        if (newPos.rank() == newRank && oldPos.rank() == startingRank && newFileDifferenceOfOne &&
                lastMove instanceof SingleMove singleMove &&
                singleMove.to().rank() == newRankOpponent && singleMove.from().rank() == startingRankOpponent
                && singleMove.to().file() == newPos.file()){
                    Piece piece = board.getPieceAt(singleMove.to());
                    return piece.getType() == PieceType.PAWN && piece.getColor() != color;
                }
        return false;
    }

    private static boolean kingNotInCheckAfterMakingMove(Board board, Move lastMove, Position kingPosition, PieceColor opposingColor) {
        for (SingleMove m: PseudoSingleMoveGenerator.generateMoves(board, opposingColor)){
            if (moveAttacksKing(m, kingPosition))
                return false;
        }
        for (EnPassantMove m: PseudoEnPassantMoveGenerator.generateMoves(board, lastMove, opposingColor)){
            if (moveAttacksKing(m, kingPosition))
                return false;
        }
        for (CastlingMove m: PseudoCastlingMoveGenerator.generateMoves(board, opposingColor))
            if (moveAttacksKing(m, kingPosition))
                return false;
        return true;
    }

    private static boolean moveAttacksKing(Move movingPiece, Position king){
        if (movingPiece instanceof EnPassantMove m)
            return m.to().equals(king);
        if (movingPiece instanceof CastlingMove m)
            return m.moveKing().to().equals(king) || m.moveRook().to().equals(king);
        return ((SingleMove) movingPiece).to().equals(king);
    }

    private static List<Move> getPseudoMoves(Board board, Move lastMove, PieceColor color) {
        List<Move> totalMoves = new ArrayList<>();
        totalMoves.addAll(PseudoSingleMoveGenerator.generateMoves(board, color));
        totalMoves.addAll(PseudoCastlingMoveGenerator.generateMoves(board, color));
        totalMoves.addAll(PseudoEnPassantMoveGenerator.generateMoves(board, lastMove, color));
        return totalMoves;
    }
}