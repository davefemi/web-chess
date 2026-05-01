package nl.davefemi.webchess;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.board.Position;
import nl.davefemi.webchess.game.rule.RuleEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class BishopMoveTest {
    private Game game;
    private final PieceColor white = PieceColor.WHITE;
    private final PieceColor black = PieceColor.BLACK;


    @BeforeEach
    public void setUp(){
        game = new Game();
    }

    private SingleMove getSingleMove(int file_start, int rank_start, int file_end, int rank_end){
        return new SingleMove(new Position(file_start, rank_start),new Position(file_end, rank_end));
    }

    @ParameterizedTest
    @CsvSource({
            "3,1,3,2, false",
            "3,1,1,3, true",
            "3,1,2,3, false",
            "3,1,4,2, true",
            "3,1,5,3, true",
            "3,1,6,4, true",
            "3,1,7,5, true",
            "3,1,8,6, true"
    })
    public void startingMoveTest(int fromFile, int fromRank, int toFile, int toRank, boolean expected) throws MoveException, BoardException, GameException {
        game.executeMove(white, getSingleMove(2,2,2,3));
        game.executeMove(black, getSingleMove(2,7,2,5));
        game.executeMove(white, getSingleMove(3,2,3,3));
        game.executeMove(black, getSingleMove(3,7,3,6));
        game.executeMove(white, getSingleMove(4,2,4,3));
        game.executeMove(black, getSingleMove(4,7,4,6));
        assertThat(RuleEngine.isMoveAllowed(game, white, getSingleMove(fromFile, fromRank, toFile, toRank))).isEqualTo(expected);
    }

}
