package nl.davefemi.webchess.game;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.move.SingleMoveDTO;
import nl.davefemi.webchess.game.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.assertj.core.api.Assertions.assertThatException;

@RequiredArgsConstructor
public class DtoMappingTest {
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
            "a1",
            "b2",
            "c5",
            "h8"
    })
    public void conversionTest(String value){
        AlgebraicSquare square = new AlgebraicSquare(value);
        Square sq = square.toSquare();
        Position pos = square.toPosition();
    }

    @ParameterizedTest
    @CsvSource({
            "a1, cannot be lower than 1",
            "b2, cannot be greater than 8",
            "c5, cannot be greater than 8",
            "d9, cannot be greater than 8",
            "d5, cannot be greater than 8"
    })
    public void validatedMoveTest(String from, String expected){
        assertThatException().isThrownBy(
                ()-> {
                    SingleMoveDTO dto = new SingleMoveDTO();

                })
                .withMessageContaining(expected);
    }

}
