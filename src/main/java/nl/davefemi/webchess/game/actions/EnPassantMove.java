package nl.davefemi.webchess.game.actions;

import nl.davefemi.webchess.game.board.AlgebraicSquare;
import nl.davefemi.webchess.game.board.Square;

public record EnPassantMove(Square from, Square to) implements Move {
    public EnPassantMove {
        if (from == null){
            throw new IllegalArgumentException("Position 'from' cannot be null");
        }
        if (to == null){
            throw new IllegalArgumentException("Position 'to'  cannot be null");
        }
        if (from.rank() == 4 || from.rank() == 3){
            int abs = Math.abs(from.value() - to.value());
            if (!(abs == 15 || (abs == 17))) {
                throw new IllegalArgumentException("Move to " + AlgebraicSquare.fromFileAndRank(to.file(), to.rank()) + " not allowed");
            }
        }
    }

}
