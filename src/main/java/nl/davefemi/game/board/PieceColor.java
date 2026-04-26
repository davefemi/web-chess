package nl.davefemi.game.board;

import lombok.Getter;

@Getter
public enum PieceColor {
    BLACK("black"),
    WHITE("white");

    private final String color;
    PieceColor(String color){
        this.color = color;
    }

    public static PieceColor fromString(String color){
        if (color == null)
            throw new IllegalArgumentException("Color cannot be null");
        for (PieceColor c : values()){
            if (c.getColor().equalsIgnoreCase(color))
                    return c;
        }
        throw new IllegalArgumentException("Color does not exist");
    }

    public static PieceColor getOpponent(PieceColor color) {
        if (color == null)
            throw new IllegalArgumentException("Color cannot be null");
        if (color == WHITE)
            return BLACK;
        return WHITE;
    }
}
