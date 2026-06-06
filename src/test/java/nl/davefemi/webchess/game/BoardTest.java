package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static nl.davefemi.webchess.game.board.PieceColor.BLACK;
import static nl.davefemi.webchess.game.board.PieceColor.WHITE;
import static org.assertj.core.api.Assertions.*;

public class BoardTest {
    private Game game;
    private BoardContext board;


    @BeforeEach
    public void setUp() throws GameException {
        game = new Game();
        game.start();
        board = game.getCurrentBoardContext();
    }

    public void tearDown(){
    }

    private SingleMove getSingleMove(int file_start, int rank_start, int file_end, int rank_end){
        return new SingleMove(Square.fromFileAndRank(file_start, rank_start), Square.fromFileAndRank(file_end, rank_end));
    }

    private Square toSquare(String position){
        return new AlgebraicSquare(position).toSquare();
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

    @Test
    public void notEnoughKingsTest() {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(1, PieceType.ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(2, PieceType.QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(3, PieceType.KING, BLACK);
        squares[toSquare("e1").value()] = new Piece(5, PieceType.QUEEN, WHITE);
        assertThatThrownBy(
                ()->
                         new Board(squares, 6))
                .hasMessageContaining("There must be exactly one king of each color on the board");
    }

    @Test
    public void twoKingsOneColour() {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(1, PieceType.ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(2, PieceType.QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(3, PieceType.KING, BLACK);
        squares[toSquare("e1").value()] = new Piece(5, PieceType.QUEEN, WHITE);
        squares[toSquare("e7").value()] = new Piece(6, PieceType.KING, BLACK);
        assertThatThrownBy(
                ()->
                        new Board(squares, 6))
                .hasMessageContaining("Board cannot have two kings of the same color");
    }

    @Test
    public void tooManyKings() {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(1, PieceType.ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(2, PieceType.QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(3, PieceType.KING, BLACK);
        squares[toSquare("f2").value()] = new Piece(3, PieceType.KING, WHITE);
        squares[toSquare("e1").value()] = new Piece(5, PieceType.QUEEN, WHITE);
        squares[toSquare("e7").value()] = new Piece(6, PieceType.KING, BLACK);
        assertThatThrownBy(
                ()->
                        new Board(squares, 6))
                .hasMessageContaining("Board cannot have two kings of the same color");
    }
}
