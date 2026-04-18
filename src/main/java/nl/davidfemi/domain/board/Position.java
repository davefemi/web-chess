package nl.davidfemi.domain.board;

import java.util.Objects;

public class Position {
    private final int file;
    private final int rank;

    public Position (int file, int rank){
        this.file = file;
        this.rank = rank;
    }

    public int getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    public String getPosition(){
        return "" + file + rank;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)){
            return false;
        }
        Position pos = (Position) o;
            return pos.getFile() == this.getFile() && pos.getRank() == this.getRank();
    }

    @Override
    public int hashCode(){
        return Objects.hash(file, rank);
    }
}
