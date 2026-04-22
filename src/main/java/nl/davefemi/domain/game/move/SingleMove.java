package nl.davefemi.domain.game.move;

import nl.davefemi.domain.board.Position;

public record SingleMove(Position from, Position to) implements Move {
}
