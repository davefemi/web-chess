package nl.davefemi.webchess.game.actions;

import nl.davefemi.webchess.game.board.Position;

public record EnPassantMove(Position from, Position to) implements Move {
    public EnPassantMove{
        if (from == null){
            throw new IllegalArgumentException("Position 'from' cannot be null");
        }
        if (to == null){
            throw new IllegalArgumentException("Position 'to'  cannot be null");
        }
    }

}
