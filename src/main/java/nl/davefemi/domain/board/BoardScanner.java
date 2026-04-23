package nl.davefemi.domain.board;

import nl.davefemi.domain.piece.Piece;
import nl.davefemi.domain.piece.PieceType;
import nl.davefemi.domain.piece.PlayerColor;
import java.util.ArrayList;
import java.util.List;

public final class BoardScanner {

    private BoardScanner(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static Position getCurrentSinglePosition(Board board, PieceType type, PlayerColor color) {
        for (Position p : board.getPositions()) {
            Piece piece = board.getPieceAt(p);
            if (piece != null && piece.getColor() == color) {
                if (piece.getType() == type) {
                    return p;
                }
            }
        }
        return null;
    }

    public static List<Position> getCurrentPositions(Board board, PieceType type, PlayerColor color) {
        List<Position> positions = new ArrayList<>();
        for (Position p : board.getPositions()) {
            Piece piece = board.getPieceAt(p);
            if (piece != null && piece.getColor() == color) {
                if (piece.getType() == type)
                    positions.add(p);
            }
        }
        return positions;
    }

    public static PlayerColor getEnemyColor(PlayerColor color){
        return color == PlayerColor.WHITE? PlayerColor.BLACK: PlayerColor.WHITE;
    }
}

