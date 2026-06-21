package nl.davefemi.chess.gameplay.model.board;

public record Square(int value) {
    public Square{
        if ((value & 0x00) != 0){
            throw new IllegalArgumentException("Position is not on board: " + value);
        }
    }

    public static Square fromFileAndRank(int file, int rank){
        if (file <0 || file > 7 || rank <0 || rank > 7){
            throw new IllegalArgumentException("Invalid file or rank");
        }
        return new Square(rank <<4 | file);
    }

    public int rank(){
        return (value >> 4);
    }

    public int file(){
        return (value & 7);
    }

}