package nl.davefemi.domain.board;

import lombok.Getter;

@Getter
public enum PlayerColor {
    BLACK("black"),
    WHITE("white");

    private final String color;
    PlayerColor(String color){
        this.color = color;
    }

    public static PlayerColor fromString(String color){
        for (PlayerColor c : values()){
            if (c.getColor().equalsIgnoreCase(color))
                    return c;
        }
        throw new IllegalArgumentException("Color does not exist");
    }
}
