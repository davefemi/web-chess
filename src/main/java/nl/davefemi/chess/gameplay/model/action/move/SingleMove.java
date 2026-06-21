package nl.davefemi.chess.gameplay.model.action.move;

import nl.davefemi.chess.gameplay.model.board.AlgebraicSquare;
import nl.davefemi.chess.gameplay.model.board.Square;

public record SingleMove(Square from, Square to) implements SinglePieceMove {
    public SingleMove {
        if (from == null){
            throw new IllegalArgumentException("Position 'from' cannot be null");
        }
        if (to == null){
            throw new IllegalArgumentException("Position 'to'  cannot be null");
        }
    }

    @Override
    public SingleMove move(){
        return this;
    }

    @Override
    public String toString(){
        return "SingleMove(from=" + AlgebraicSquare.fromFileAndRank(from().file(), from().rank()).value() + ", to=" +
                AlgebraicSquare.fromFileAndRank(to().file(), to().rank()).value() + ")";
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof SingleMove(Square from1, Square v)){
            return from.value() == from1.value() && to.value() == v.value();
        }
        return false;
    }
}
