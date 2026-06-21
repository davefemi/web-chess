package nl.davefemi.chess.gameplay.model.action.move;

import nl.davefemi.chess.exception.TypeException;

public enum MoveType {
    SINGLE("single"),
    ENPASSANT("enpassant"),
    PROMOTION("promotion"),
    CASTLING("castling");

    private final String type;
    MoveType(String type){
        this.type = type;
    }

    public static MoveType fromString(String type){
        for (MoveType t : MoveType.values()){
            if (t.type.equalsIgnoreCase(type))
                return t;
        }
        throw new TypeException("Move type does not exist");
    }

    @Override
    public String toString(){
        return type;
    }
}
