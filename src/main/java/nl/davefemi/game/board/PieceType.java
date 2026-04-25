package nl.davefemi.game.board;

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

    public static PieceType fromString(String type){
        for (PieceType t : values()){
            if (t.label.equalsIgnoreCase(type))
                return t;
        }
        throw new IllegalArgumentException("Type does not exist");
    }

}
