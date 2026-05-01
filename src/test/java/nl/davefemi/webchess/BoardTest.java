package nl.davefemi.webchess;

import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.IdGenerator;
import nl.davefemi.webchess.game.actions.SingleMove;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;


@SpringBootTest
public class BoardTest {
    private Game game;
    private Board board;


    @BeforeEach
    public void setUp(){
        game = new Game();
        board = new Board(new IdGenerator());

    }

    @AfterEach
    public void tearDown(){
    }

    private SingleMove getSingleMove(int file_start, int rank_start, int file_end, int rank_end){
        return new SingleMove(new Position(file_start, rank_start),new Position(file_end, rank_end));
    }

    @ParameterizedTest
    @CsvSource({
            "0,0,0,0, does not exist",
            "2,2, 2,9, does not exist",
            "2,2, 9,2, does not exist",
            "9,2, 2,2, does not exist",
            "2,9, 2,2, does not exist"
    })
    public void validatedMoveTest(int fromFile, int fromRank, int toFile, int toRank, String expected){
        assertThatException().isThrownBy(
                ()-> board.applyValidatedMove(getSingleMove(fromFile, fromRank, toFile, toRank)))
                .withMessageContaining(expected);
    }

    @Test
    public void promotionIdTest(){
        assertThat(game.getLatestPieceId()).isGreaterThan(new IdGenerator().getNextId());
        assertThatException().isThrownBy(
                ()-> board.applyValidatedMove(
                        new IdGenerator(),
                        new PromotionMove(
                                 getSingleMove(1,1,2,2),
                                PieceType.QUEEN))).withMessageContaining("id");
    }
}
