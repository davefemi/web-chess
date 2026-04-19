package nl.davidfemi.domain.game;

import nl.davidfemi.domain.board.Position;

public record Move(Position oldPosition, Position newPosition) {

}
