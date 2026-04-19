package nl.davidfemi;

import nl.davidfemi.domain.board.Position;
import nl.davidfemi.domain.game.Game;
import nl.davidfemi.domain.game.Move;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Game game =  new Game();
        game.setMove(new Move(new Position(3,1), new Position(4,2)));
        game.setMove(new Move(new Position(2,7), new Position(2,6)));
        game.setMove(new Move(new Position(2,1), new Position(3,3)));
        game.setMove(new Move(new Position(2,6), new Position(2,5)));
        game.setMove(new Move(new Position(8,2), new Position(8,3)));
        game.setMove(new Move(new Position(2,5), new Position(2,4)));
        game.setMove(new Move(new Position(8,3), new Position(8,4)));
        game.setMove(new Move(new Position(2,4), new Position(3,3)));

        game.printBoard();
        }
}
