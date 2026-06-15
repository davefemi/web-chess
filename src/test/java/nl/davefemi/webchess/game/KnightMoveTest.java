package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.AlgebraicSquare;
import nl.davefemi.webchess.game.rule.RuleEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static nl.davefemi.webchess.game.Color.BLACK;
import static nl.davefemi.webchess.game.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

public class KnightMoveTest {
    private Game game;

    @BeforeEach
    public void setUp() throws GameException {
        game = new Game();
        game.start();
    }

    private SingleMove getSingleMove(String from, String to){
        return new SingleMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }

    @ParameterizedTest
    @CsvSource
            ({
                    "b1,a3, true",
                    "b1,c3, true",
                    "b1,d3, false",
                    "b1,d2, true",
                    "g1,e2, true"
            })
    public void startingMoveTest(String from, String to, boolean expected) throws MoveException, BoardException, GameException {
        game.executeMove(WHITE, getSingleMove("d2","d4"));
        game.executeMove(BLACK, getSingleMove("a7","a6"));
        game.executeMove(WHITE, getSingleMove("e2","e4"));
        game.executeMove(BLACK, getSingleMove("a6","a5"));
        assertThat(RuleEngine.isMoveAllowed(game.getGameBoardContext(), game.getMoveHistory(), WHITE, getSingleMove(from, to))).isEqualTo(expected);
    }
}
