package nl.davefemi.domain.piece;

public enum PieceType {
    PAWN("pawn"),
    ROOK("rook"),
    KNIGHT("knight"),
    BISHOP("bishop"),
    QUEEN("queen"),
    KING("king");

    private final String label;

    PieceType(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }

}
