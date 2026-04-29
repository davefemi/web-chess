package nl.davefemi.webchess.game.board;

import java.util.ArrayList;
import java.util.List;

public final class BoardScanner {

    private BoardScanner(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static Position getCurrentSinglePiecePosition(Board board, PieceType type, PieceColor color) {
        for (Position p : board.getBoardPositions()) {
            Piece piece = board.getPieceAt(p);
            if (piece != null && piece.getColor() == color) {
                if (piece.getType() == type) {
                    return p;
                }
            }
        }
        return null;
    }

    public static List<Position> getCurrentPiecePositions(Board board, PieceType type, PieceColor color) {
        List<Position> positions = new ArrayList<>();
        for (Position p : board.getBoardPositions()) {
            Piece piece = board.getPieceAt(p);
            if (piece != null && piece.getColor() == color) {
                if (piece.getType() == type)
                    positions.add(p);
            }
        }
        return positions;
    }
}