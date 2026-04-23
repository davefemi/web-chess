package nl.davefemi.domain.game.rule;

import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.board.BoardScanner;
import nl.davefemi.domain.board.Position;
import nl.davefemi.domain.game.actions.move.CastlingMove;
import nl.davefemi.domain.game.actions.move.Move;
import nl.davefemi.domain.game.actions.record.CastlingMoveRecord;
import nl.davefemi.domain.game.actions.record.MoveRecord;
import nl.davefemi.domain.game.actions.move.SingleMove;
import nl.davefemi.domain.game.actions.record.SingleMoveRecord;
import nl.davefemi.domain.game.utility.CastlingMoveGenerator;
import nl.davefemi.domain.game.utility.MoveGenerator;
import nl.davefemi.domain.board.Piece;
import nl.davefemi.domain.board.PieceType;
import nl.davefemi.domain.board.PlayerColor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.MoveException;

import java.util.ArrayList;
import java.util.List;

public final class MoveEvaluator {

    private MoveEvaluator(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static boolean isCastlingLegal(Board board, List<MoveRecord> moveHistory, CastlingMove move) throws MoveException {
        Piece king = board.getPieceAt(move.moveKing().from());
        Piece rook = board.getPieceAt(move.moveRook().from());
        if (king != null && king.getType() == PieceType.KING &&
                rook != null && rook.getType() == PieceType.ROOK){
            if (!board.isOriginalRook(rook.getId()))
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

    public static List<CastlingMove> filterAgainstCheckAfterCastling(Board board, List<CastlingMove> pseudoMoves, PlayerColor enemyColor) throws BoardException {
        List<CastlingMove> legalMoves = new ArrayList<>();
        for (CastlingMove c : pseudoMoves) {
            if (!MoveEvaluator.isKingCheck(MoveEvaluator.fictitiousMove(board, c.moveKing()), c.moveKing().to(), enemyColor))
                legalMoves.add(c);
        }
        return legalMoves;
    }

    public static List<SingleMove> filterAgainstCheckAfterMove(Board board, List<SingleMove> pseudoMoves, PlayerColor enemyColor) throws BoardException {
        List<SingleMove> legalMoves = new ArrayList<>();
        for (SingleMove m : pseudoMoves) {
            Board fictitiousBoard = fictitiousMove(board, m);
            Position kingPosition = BoardScanner.getCurrentSinglePosition(fictitiousBoard, PieceType.KING, enemyColor == PlayerColor.WHITE
                    ?PlayerColor.BLACK
                    :PlayerColor.WHITE);
            if (!MoveEvaluator.isKingCheck(fictitiousBoard, kingPosition, enemyColor))
                legalMoves.add(m);
        }
        return legalMoves;
    }

    public static boolean isKingCheck(Board board, Position kingPosition, PlayerColor enemyColor) throws BoardException {
        for (SingleMove m: MoveGenerator.generateMoves(board, enemyColor, false)){
            if (m.to().equals(kingPosition))
                return true;
        }
        for (CastlingMove m: CastlingMoveGenerator.generateCastlingMoves(board, enemyColor, false))
            if (m.moveKing().to().equals(kingPosition) || m.moveRook().to().equals(kingPosition))
                return true;
        return false;
    }

    public static boolean filterForBlockingCapturePositions(Board board, Position oldPos, Position newPos, List<SingleMove> moves){
        if (board.isOccupied(newPos)) {
            if (board.getPieceAt(oldPos).getColor() == board.getPieceAt(newPos).getColor())
                return true;
            moves.add(new SingleMove(oldPos, newPos));
            return true;
        }
        return false;
    }

    public static boolean isPawnMoveLegal(Board board, Position oldPos, Position newPos){
        if (board.isOccupied(newPos) &&
                board.getPieceAt(newPos).getColor() != board.getPieceAt(oldPos).getColor() &&
                oldPos.file() != newPos.file())
            return true;
        return (!board.isOccupied(newPos) && oldPos.file() == newPos.file());
    }

    public static Board fictitiousMove(Board board, Move move) throws BoardException {
        Board newBoard = new Board(board);
            newBoard.movePieceTo(move);
        return newBoard;
    }
}