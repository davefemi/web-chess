package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.List;
import static nl.davefemi.webchess.game.Color.BLACK;
import static nl.davefemi.webchess.game.Color.WHITE;
import static nl.davefemi.webchess.game.board.PieceType.*;
import static org.assertj.core.api.Assertions.*;

public class BoardTest {
    private Game game;
    private GameBoardContext board;


    @BeforeEach
    public void setUp() throws GameException {
        game = new Game();
        game.start();
        board = game.getGameBoardContext();
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
                ()-> board.applyValidatedMove(getSingleMove(fromFile, fromRank, toFile, toRank)))
                .withMessageContaining(expected);
    }

    @Test
    public void promotionIdTest(){
        assertThat(game.getGameBoardContext().getCopyOfBoard().getNextPieceId()).isGreaterThan(new IdGenerator().getNextId());
    }

    @Test
    public void boardAssessmentTest() {
        //Arrange
        List<Piece> pieces = board.getCopyOfBoard().getPieces();
        int whitePawn = 0;
        int whiteRook = 0;
        int whiteKnight = 0;
        int whiteBishop = 0;
        int whiteQueen = 0;
        int whiteKing = 0;
        int blackPawn = 0;
        int blackRook = 0;
        int blackKnight = 0;
        int blackBishop = 0;
        int blackQueen = 0;
        int blackKing = 0;
        int total = 0;

        //Act
        for (Piece p: pieces) {
            boolean white = p.color() == WHITE;
            if (p.type() == PAWN){
                if(white)
                    whitePawn++;
                else
                    blackPawn++;
                total++;
            }
            if (p.type() == ROOK){
                if (white)
                    whiteRook++;
                else
                    blackRook++;
                total++;
            }
            if(p.type() == KNIGHT){
                if(white)
                    whiteKnight++;
                else
                    blackKnight++;
                total++;
            }
            if(p.type() == BISHOP){
                if(white)
                    whiteBishop++;
                else
                    blackBishop++;
                total++;
            }
            if(p.type() == QUEEN){
                if (white)
                    whiteQueen++;
                else
                    blackQueen++;
                total++;
            }
            if(p.type() == KING){
                if(white)
                    whiteKing++;
                else
                    blackKing++;
                total++;
            }
        }

        //Assert
        assertThat(whitePawn).isEqualTo(blackPawn).isEqualTo(8);
        assertThat(whiteRook).isEqualTo(blackRook).isEqualTo(2);
        assertThat(whiteKnight).isEqualTo(blackKnight).isEqualTo(2);
        assertThat(whiteBishop).isEqualTo(blackBishop).isEqualTo(2);
        assertThat(whiteQueen).isEqualTo(blackQueen).isEqualTo(1);
        assertThat(whiteKing).isEqualTo(blackKing).isEqualTo(1);
        assertThat(total).isEqualTo(pieces.size()).isEqualTo(32);
    }

    @Test
    public void notEnoughKingsTest() {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(101, ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(102, QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(103, KING, BLACK);
        squares[toSquare("e1").value()] = new Piece(105, QUEEN, WHITE);
        assertThatThrownBy(
                ()->
                         new Board(squares, 106))
                .hasMessageContaining("There must be exactly one king of each playerColor on the board");
    }

    @Test
    public void twoKingsOneColour() {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(101, ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(102, QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(103, KING, BLACK);
        squares[toSquare("e1").value()] = new Piece(105, QUEEN, WHITE);
        squares[toSquare("e7").value()] = new Piece(106, KING, BLACK);
        assertThatThrownBy(
                ()->
                        new Board(squares, 107))
                .hasMessageContaining("Board cannot have two kings of the same playerColor");
    }

    @Test
    public void tooManyKings() {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(101, ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(102, QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(103, KING, BLACK);
        squares[toSquare("f2").value()] = new Piece(103, KING, WHITE);
        squares[toSquare("e1").value()] = new Piece(105, QUEEN, WHITE);
        squares[toSquare("e7").value()] = new Piece(106, KING, BLACK);
        assertThatThrownBy(
                ()->
                        new Board(squares, 107))
                .hasMessageContaining("Board cannot have two kings of the same playerColor");
    }

    @Test
    public void unicityTest() {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(101, ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(101, QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(103, KING, BLACK);
        squares[toSquare("e1").value()] = new Piece(105, QUEEN, WHITE);
        squares[toSquare("e7").value()] = new Piece(106, KING, WHITE);
        assertThatThrownBy(
                ()->
                        new Board(squares, 107))
                .hasMessageContaining("Pieces are not unique");
    }

    @Test
    public void nextIdTest() {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(101, ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(140, QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(103, KING, BLACK);
        squares[toSquare("e1").value()] = new Piece(105, QUEEN, WHITE);
        squares[toSquare("e7").value()] = new Piece(104, KING, WHITE);
        assertThatThrownBy(
                ()->
                        new Board(squares, 120))
                .hasMessageContaining("Next id must be higher than 140");
    }
}
