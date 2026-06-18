package nl.davefemi.chess.play.model.board;

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

    @Override
    public String toString(){
        return label;
    }

    public static PieceType fromString(String type){
        for (PieceType t : values()){
            if (type.toLowerCase().contains(t.label))
                return t;
        }
        throw new IllegalArgumentException("Type does not exist");
    }

}
