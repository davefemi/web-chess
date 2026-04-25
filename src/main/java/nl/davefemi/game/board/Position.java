package nl.davefemi.game.board;

public record Position(int file, int rank) implements Comparable<Object> {

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
