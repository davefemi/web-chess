package nl.davefemi.domain.game.rule;

import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.board.BoardScanner;
import nl.davefemi.domain.board.Position;
import nl.davefemi.domain.game.move.CastlingMove;
import nl.davefemi.domain.game.move.Move;
import nl.davefemi.domain.game.move.SingleMove;
import nl.davefemi.domain.game.utility.CastlingMoveGenerator;
import nl.davefemi.domain.game.utility.MoveGenerator;
import nl.davefemi.domain.piece.PieceType;
import nl.davefemi.domain.piece.PlayerColor;
import java.util.ArrayList;
import java.util.List;

public final class MoveEvaluator {

    private MoveEvaluator(){
        throw new AssertionError("This class cannot be instantiated");
    }


    public static List<CastlingMove> filterAgainstCheckAfterCastling(Board board, List<CastlingMove> pseudoMoves, PlayerColor enemyColor){
        List<CastlingMove> legalMoves = new ArrayList<>();
        for (CastlingMove c : pseudoMoves) {
            if (!MoveEvaluator.isKingCheck(MoveEvaluator.fictitiousMove(board, c.moveKing()), c.moveKing().to(), enemyColor))
                legalMoves.add(c);
        }
        return legalMoves;
    }

    public static List<SingleMove> filterAgainstCheckAfterMove(Board board, List<SingleMove> pseudoMoves, PlayerColor enemyColor){
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

    public static boolean isKingCheck(Board board, Position kingPosition, PlayerColor enemyColor){
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

    public static Board fictitiousMove(Board board, Move move){
        Board newBoard = new Board(board);
            newBoard.movePieceTo(move);
        return newBoard;
    }
}