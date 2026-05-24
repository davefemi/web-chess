package nl.davefemi.webchess.game.board;

public record Position(int file, int rank) implements Comparable<Object> {
    public Position{
        if (file < 1 || rank < 1){
            throw new IllegalArgumentException("File or rank cannot be lower than 1");
        }
        if (file > 8 || rank > 8){
            throw new IllegalArgumentException("File or rank cannot be greater than 8");
        }
    }

    public String getPosition() {
        return "" + file + rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position(int file1, int rank1))) {
            return false;
        }
        return file1 == this.file() && rank1 == this.rank();
    }

    @Override
    public int compareTo(Object o) {
        Position pos = (Position) o;
        if (this.rank() < pos.rank())
            return -1;
        if (this.rank() > pos.rank())
            return 1;
        if (this.file() < pos.file())
            return -1;
        if (this.file() > pos.file())
            return 1;
        return 0;
    }
}
