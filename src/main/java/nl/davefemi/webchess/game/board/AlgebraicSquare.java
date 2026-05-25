package nl.davefemi.webchess.game.board;

public record AlgebraicSquare(String value) {
    public AlgebraicSquare{
        if (value == null || !value.matches("[a-h][1-8]")){
            throw new IllegalArgumentException("Value must be of form [a-h][1-8]");
        }
    }

    public Square toSquare(){
        int file = value.charAt(0) - 'a';
        int rank = value.charAt(1) - '1';
        return Square.fromFileAndRank(file, rank);
    }

    public static AlgebraicSquare fromFileAndRank(int file, int rank){
        if (file <0 || file > 7 || rank <0 || rank > 7){
            throw new IllegalArgumentException("Invalid file or rank");
        }
        return new AlgebraicSquare("" + (char) ('a' + file) + (rank+1));
    }

    public Position toPosition(){
        int file = value.charAt(0) -'a' + 1;
        int rank = value.charAt(1) - '1' + 1;
        return new Position(file, rank);
    }
}
