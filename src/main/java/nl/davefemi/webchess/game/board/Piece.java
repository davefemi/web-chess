package nl.davefemi.webchess.game.board;

import nl.davefemi.webchess.game.Color;

public record Piece(int id, PieceType type, Color color) {
    public Piece {
        if (type == null)
            throw new IllegalArgumentException("Type cannot be null");
        if (color == null)
            throw new IllegalArgumentException("Color cannot be null");
    }
}
