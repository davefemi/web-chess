package nl.davefemi.webchess.game;

import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.actions.SingleMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;

public class BoardTest {
    private Game game;
    private Board board;


    @BeforeEach
    public void setUp(){
        game = new Game();
        board = new Board();

    }

    public void tearDown(){
    }

    private SingleMove getSingleMove(int file_start, int rank_start, int file_end, int rank_end){
        return new SingleMove(new Position(file_start, rank_start),new Position(file_end, rank_end));
    }

    @ParameterizedTest
    @CsvSource({
            "0,0,0,0, cannot be lower than 1",
            "2,2, 2,9, cannot be greater than 8",
            "2,2, 9,2, cannot be greater than 8",
            "9,2, 2,2, cannot be greater than 8",
            "2,9, 2,2, cannot be greater than 8"
    })
    public void validatedMoveTest(int fromFile, int fromRank, int toFile, int toRank, String expected){
        assertThatException().isThrownBy(
                ()-> board.applyValidatedMove(getSingleMove(fromFile, fromRank, toFile, toRank)))
                .withMessageContaining(expected);
    }

    @Test
    public void promotionIdTest(){
        assertThat(game.getCurrentBoardContext().getCopyOfBoard().getNextPieceId()).isGreaterThan(new IdGenerator().getNextId());
        assertThatException().isThrownBy(
                ()-> board.applyValidatedMove(
                        new PromotionMove(
                                 getSingleMove(1,1,2,2),
                                PieceType.QUEEN))).withMessageContaining("id");
    }
}
