package nl.davefemi.webchess.game;

import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

public class BoardTest {
    private Game game;
    private BoardContext board;


    @BeforeEach
    public void setUp(){
        game = new Game();
        board = game.getCurrentBoardContext();
    }

    public void tearDown(){
    }

    private SingleMove getSingleMove(int file_start, int rank_start, int file_end, int rank_end){
        return new SingleMove(Square.fromFileAndRank(file_start, rank_start), Square.fromFileAndRank(file_end, rank_end));
    }

    @ParameterizedTest
    @CsvSource({
            "-1,0,0,0, Invalid file or rank",
            "2,2, 2,9, Invalid file or rank",
            "2,2, 9,2, Invalid file or rank",
            "9,2, 2,2, Invalid file or rank",
            "2,9, 2,2, Invalid file or rank"
    })
    public void validatedMoveTest(int fromFile, int fromRank, int toFile, int toRank, String expected){
        assertThatException().isThrownBy(
                ()-> board.applyMove(getSingleMove(fromFile, fromRank, toFile, toRank)))
                .withMessageContaining(expected);
    }

    @Test
    public void promotionIdTest(){
        assertThat(game.getCurrentBoardContext().getCopyOfBoard().getNextPieceId()).isGreaterThan(new IdGenerator().getNextId());
    }
}
