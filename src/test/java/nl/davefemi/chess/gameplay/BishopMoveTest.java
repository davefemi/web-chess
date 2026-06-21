package nl.davefemi.chess.gameplay;

import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.GameException;
import nl.davefemi.chess.exception.MoveException;
import nl.davefemi.chess.gameplay.model.actions.move.SingleMove;
import nl.davefemi.chess.gameplay.model.board.AlgebraicSquare;
import nl.davefemi.chess.gameplay.model.game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static nl.davefemi.chess.gameplay.model.game.Color.BLACK;
import static nl.davefemi.chess.gameplay.model.game.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

public class BishopMoveTest {
    private Game game;

    @BeforeEach
    public void setUp() throws GameException {
        game = new Game("");
        game.start();
    }

    private SingleMove getSingleMove(String from, String to){
        return new SingleMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }

    @ParameterizedTest
    @CsvSource({
            "c1,a3, true",
            "c1,d2, true",
            "c1,e3, true",
            "c1,f4, true",
            "c1,g5, true",
            "c1,h6, true"
    })
    public void startingMoveTest(String from, String to, boolean expected) throws MoveException, BoardException, GameException {
        game.executeMove(WHITE, getSingleMove("b2","b3"));
        game.executeMove(BLACK, getSingleMove("b7","b5"));
        game.executeMove(WHITE, getSingleMove("c2","c3"));
        game.executeMove(BLACK, getSingleMove("c7","c6"));
        game.executeMove(WHITE, getSingleMove("d2","d3"));
        game.executeMove(BLACK, getSingleMove("d7","d6"));
        assertThat(game.executeMove(WHITE, getSingleMove(from, to))).isEqualTo(expected);
    }
}
