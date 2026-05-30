package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.Board;
import nl.davefemi.webchess.game.board.PieceColor;
import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {
    private static final int PAWN_MOV_OFFSET = 16;
    private static final int[] PAWN_ATT_OFFSET = {15,17};
    private static final int[] ROOK_OFFSET = {1,16};
    private static final int[] KNIGHT_OFFSET = {14,18,31,33};
    private static final int[] BISHOP_OFFSET = {15, 17};
    private static final int[] QUEEN_OFFSET = {1,15,16,17};
    private static final int[] KING_OFFSET = {1,16};

    static List<SingleMove> generateMoves(Board board, PieceColor color) {
        List<SingleMove> moves = new ArrayList<>();
//        moves.addAll(getKingMoves(board, BoardScanner.getCurrentSinglePiecePosition(board, PieceType.KING, color)));
//        moves.addAll(getQueenMoves(board, BoardScanner.getCurrentPiecePositions(board, PieceType.QUEEN, color)));
//        moves.addAll(getBishopMoves(board, BoardScanner.getCurrentPiecePositions(board, PieceType.BISHOP, color)));
//        moves.addAll(getRookMoves(board, BoardScanner.getCurrentPiecePositions(board, PieceType.ROOK, color)));
//        moves.addAll(getKnightMoves(board, BoardScanner.getCurrentPiecePositions(board, PieceType.KNIGHT, color)));
//        moves.addAll(getPawnMoves(board, BoardScanner.getCurrentPiecePositions(board, PieceType.PAWN, color), color));
        return moves;
    }

    private static List<SingleMove> getKingMoves(Board board, int position){
        return null;
    }
}
