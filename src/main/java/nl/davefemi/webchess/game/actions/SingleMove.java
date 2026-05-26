package nl.davefemi.webchess.game.actions;

import nl.davefemi.webchess.game.board.Square;

public record SingleMove(Square from, Square to) implements Move {
    public SingleMove {
        if (from == null){
            throw new IllegalArgumentException("Position 'from' cannot be null");
        }
        if (to == null){
            throw new IllegalArgumentException("Position 'to'  cannot be null");
        }
    }
}
