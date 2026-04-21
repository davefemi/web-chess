package nl.davidfemi.domain.board;

import nl.davidfemi.domain.pieces.Piece;
import nl.davidfemi.domain.pieces.PieceType;
import nl.davidfemi.domain.pieces.PlayerColor;
import java.util.ArrayList;
import java.util.List;

public final class BoardScanner {

    private BoardScanner(){
        throw new AssertionError("This class cannot be instantiated");
    }

    public static Position getPosition(Board board, PieceType type, PlayerColor color) {
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

    public static List<Position> getPositions(Board board, PieceType type, PlayerColor color) {
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

