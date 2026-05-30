package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.AlgebraicSquare;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.rule.RuleEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.assertThat;

public class KnightMoveTest {
    private Game game;
    private final PieceColor white = PieceColor.WHITE;
    private final PieceColor black = PieceColor.BLACK;


    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    private SingleMove getSingleMove(String from, String to){
        return new SingleMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }

    @ParameterizedTest
    @CsvSource({
            "b1,a3, true",
            "b1,c3, true",
            "b1,d3, false",
            "b1,d2, true",
            "g1,e2, true"
    })
    public void startingMoveTest(String from, String to, boolean expected) throws MoveException, BoardException, GameException {
        game.executeMove(white, getSingleMove("d2","d4"));
        game.executeMove(black, getSingleMove("a7","a6"));
        game.executeMove(white, getSingleMove("e2","e4"));
        game.executeMove(black, getSingleMove("a6","a5"));
        assertThat(RuleEngine.isMoveAllowed(game.getCurrentBoardContext(), game.getMoveHistory(), white, getSingleMove(from, to))).isEqualTo(expected);
    }

}
