package nl.davefemi.webchess.game.actions;

import nl.davefemi.webchess.game.board.Position;

public record EnPassantMove(Position from, Position to) implements Move {

}
