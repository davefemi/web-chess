package nl.davefemi.chess.gameplay.model.game;

public enum Color {
    BLACK("black"),
    WHITE("white");

    private final String color;
    Color(String color){
        this.color = color;
    }

    public static Color fromString(String color){
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        for (Color c : values()){
            if (color.toLowerCase().contains(c.color)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Color does not exist");
    }

    public static Color getOpponent(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("Color cannot be null");
        }
        if (color == WHITE) {
            return BLACK;
        }
        return WHITE;
    }

    @Override
    public String toString(){
        return color;
    }
}
