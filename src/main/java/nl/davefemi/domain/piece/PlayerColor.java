package nl.davefemi.domain.piece;

import lombok.Getter;

@Getter
public enum PlayerColor {
    BLACK("black"),
    WHITE("white");

    private final String color;
    PlayerColor(String color){
        this.color = color;
    }
}
