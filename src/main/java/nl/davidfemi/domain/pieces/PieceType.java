package nl.davidfemi.domain.pieces;

public enum PieceType {
    PAWN("pawn"),
    ROOK("rook"),
    KNIGHT("knight"),
    BISHOP("bishop"),
    QUEEN("queen"),
    KING("king");

    private String label;

    private PieceType(String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
