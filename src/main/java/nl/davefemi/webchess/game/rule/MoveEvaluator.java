package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.board.Board;
import nl.davefemi.webchess.game.board.Position;
import nl.davefemi.webchess.game.actions.CastlingMove;
import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.record.CastlingMoveRecord;
import nl.davefemi.webchess.game.record.MoveRecord;
import nl.davefemi.webchess.game.actions.SingleMove;
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

    static List<Move> evaluateIfKingIsInCheckAfterMove(Game game, List<Move> pseudoMoves, PieceColor player) throws BoardException {
        PieceColor opponent = PieceColor.getOpponent(player);
        List<Move> legalMoves = new ArrayList<>();
        for (Move initialMove : pseudoMoves) {
            boolean initialKingIsCheckAfterMove = false;
            Board boardAfterInitialMove = PseudoSingleMoveGenerator.applyFictitiousMove(game.getCopyOfBoard(), initialMove);
            Position kingOfInitialPlayer = boardAfterInitialMove.getPositionsByTypeAndColor(PieceType.KING, player).getFirst();
            for (Move opponentMove : getPseudoMoves(boardAfterInitialMove,initialMove, PieceColor.getOpponent(player))) {
                Board boardAfterOpponentMove = PseudoSingleMoveGenerator.applyFictitiousMove(boardAfterInitialMove, opponentMove);
                List<Position> boardPositions = boardAfterOpponentMove.getPositionsByTypeAndColor(PieceType.KING, opponent);
                if (boardPositions.isEmpty())
                    continue;
                Position positionOpponentKing = boardPositions.getFirst();
                if (opponentMove instanceof SingleMove singleCounterMoveOpponent) {
                    if (singleCounterMoveOpponent.to().equals(kingOfInitialPlayer) &&
                            !isKingInCheck(boardAfterOpponentMove, opponentMove, positionOpponentKing, player)) {
                        initialKingIsCheckAfterMove = true;
                        break;
                    }
                    continue;
                }
                if (opponentMove instanceof CastlingMove castlingCounterMoveOpponent){
                    if ((castlingCounterMoveOpponent.moveKing().to().equals(kingOfInitialPlayer) ||
                            castlingCounterMoveOpponent.moveRook().to().equals(kingOfInitialPlayer)) &&
                                    !isKingInCheck(boardAfterOpponentMove, opponentMove, positionOpponentKing, player)){
                        initialKingIsCheckAfterMove = true;
                        break;
                    }
                }
            }
            if (initialKingIsCheckAfterMove)
                continue;
            legalMoves.add(initialMove);
        }
        return legalMoves;
    }

    static boolean isKingInCheck(Board board, Move lastMove, Position kingPosition, PieceColor opposingColor) {
        for (SingleMove m: PseudoSingleMoveGenerator.generateMoves(board, lastMove, opposingColor)){
            if (m.to().equals(kingPosition))
                return true;
        }
        for (CastlingMove m: PseudoCastlingMoveGenerator.generateMoves(board, opposingColor))
            if (m.moveKing().to().equals(kingPosition) || m.moveRook().to().equals(kingPosition))
                return true;
        return false;
    }

    private static List<Move> getPseudoMoves(Board board, Move lastMove, PieceColor color) {
        List<Move> totalMoves = new ArrayList<>();
        totalMoves.addAll(PseudoSingleMoveGenerator.generateMoves(board, lastMove, color));
        totalMoves.addAll(PseudoCastlingMoveGenerator.generateMoves(board, color));
        return totalMoves;
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

    static boolean isPawnMoveLegal(Board board, Move lastMove, Position oldPos, Position newPos) {
        if (board.isBoardPositionOccupied(newPos) &&
                board.getPieceAt(newPos).getColor() != board.getPieceAt(oldPos).getColor() &&
                oldPos.file() != newPos.file())
            return true;
        return (!board.isBoardPositionOccupied(newPos) && oldPos.file() == newPos.file());
    }
}