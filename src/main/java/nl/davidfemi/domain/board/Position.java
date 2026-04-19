package nl.davidfemi.domain.board;

public record Position(int file, int rank) implements Comparable {

    public String getPosition() {
        return "" + file + rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        Position pos = (Position) o;
        return pos.file() == this.file() && pos.rank() == this.rank();
    }

    @Override
    public int compareTo(Object o) {
        Position pos = (Position) o;
        if (this.rank() < pos.rank())
            return -1;
        if (this.rank() > pos.rank())
            return 1;
        if (this.rank() == pos.rank()) {
            if (this.file() < pos.file())
                return -1;
            if (this.file() > pos.file())
                return 1;
        }
        return 0;
    }
}
