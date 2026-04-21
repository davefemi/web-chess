package nl.davidfemi.domain.pieces;

public enum PlayerColor {
    BLACK("black"),
    WHITE("white");

    private String color;
    private PlayerColor(String color){
        this.color = color;
    }
    public String getColor() {
        return color;
    }
}
