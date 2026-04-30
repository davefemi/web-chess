package nl.davefemi.webchess.game.actions;

import lombok.Getter;
import nl.davefemi.webchess.game.board.Position;

@Getter
public final class SingleMoveEnpassant implements Move {
    private final Position from;
    private final Position to;
    private boolean enPassant = true;

      public SingleMoveEnpassant (Position from, Position to){
          this.from = from;
          this.to = to;
      }
}
