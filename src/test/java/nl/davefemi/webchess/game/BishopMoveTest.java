package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.AlgebraicSquare;
import nl.davefemi.webchess.game.board.PieceColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;


public class BishopMoveTest {
    private Game game;
    private final PieceColor white = PieceColor.WHITE;
    private final PieceColor black = PieceColor.BLACK;


    @BeforeEach
    public void setUp(){
        game = new Game();
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
        game.executeMove(white, getSingleMove("b2","b3"));
        game.executeMove(black, getSingleMove("b7","b5"));
        game.executeMove(white, getSingleMove("c2","c3"));
        game.executeMove(black, getSingleMove("c7","c6"));
        game.executeMove(white, getSingleMove("d2","d3"));
        game.executeMove(black, getSingleMove("d7","d6"));
        assertThat(game.executeMove(white, getSingleMove(from, to))).isEqualTo(expected);
    }

}
