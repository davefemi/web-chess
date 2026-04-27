package nl.davefemi.webchess.game.actions;

import nl.davefemi.webchess.game.board.Position;

public record SingleMove(Position from, Position to) implements Move {
}
