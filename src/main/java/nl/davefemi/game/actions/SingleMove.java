package nl.davefemi.game.actions;

import nl.davefemi.game.board.Position;

public record SingleMove(Position from, Position to) implements Move {
}
