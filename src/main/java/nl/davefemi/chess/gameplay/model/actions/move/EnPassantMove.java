package nl.davefemi.chess.gameplay.model.actions.move;

import nl.davefemi.chess.gameplay.model.board.AlgebraicSquare;
import nl.davefemi.chess.gameplay.model.board.Square;

public record EnPassantMove(Square from, Square to) implements SinglePieceMove {
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

    @Override
    public SingleMove move() {
        return new SingleMove(from, to);
    }
}
