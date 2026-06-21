package nl.davefemi.chess.gameplay;

import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.GameException;
import nl.davefemi.chess.exception.MoveException;
import nl.davefemi.chess.gameplay.model.action.move.CastlingMove;
import nl.davefemi.chess.gameplay.model.action.move.SingleMove;
import nl.davefemi.chess.gameplay.model.board.*;
import nl.davefemi.chess.gameplay.model.game.Color;
import nl.davefemi.chess.gameplay.model.game.Game;
import nl.davefemi.chess.gameplay.model.game.GameStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static nl.davefemi.chess.gameplay.model.board.PieceType.*;
import static nl.davefemi.chess.gameplay.model.game.Color.BLACK;
import static nl.davefemi.chess.gameplay.model.game.Color.WHITE;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CastlingMoveTest {
    private Game game;
    private Board board ;

    @BeforeEach
    public void setUp() throws GameException {
        game = new Game("");
        game.start();
        board = game.getGameBoardContext().getCopyOfBoard();
    }

    private SingleMove getSingleMove(String from, String to){
        return new SingleMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }

    private CastlingMove getCastlingMove(AlgebraicSquare king_from, AlgebraicSquare king_to,
                                       AlgebraicSquare rook_from, AlgebraicSquare rook_to){
        return new CastlingMove(new SingleMove(king_from.toSquare(), king_to.toSquare()),
                new SingleMove(rook_from.toSquare(), rook_to.toSquare()));
    }

    private Square toSquare(String position){
        return new AlgebraicSquare(position).toSquare();
    }


    @Test
    public void castlingNotAllowed(){
        Exception exception = assertThrows(MoveException.class, () ->{
            //Arrange
            game.executeMove(WHITE, getSingleMove("e2","e3"));
            game.executeMove(BLACK, getSingleMove("e7","e6"));
            game.executeMove(WHITE, getSingleMove("f1","d3"));
            game.executeMove(BLACK, getSingleMove("f8","d6"));
            game.executeMove(WHITE, getSingleMove("g1","h3"));
            game.executeMove(BLACK, getSingleMove("g8","h6"));
            game.executeMove(WHITE, getSingleMove("e1","f1"));
            game.executeMove(BLACK, getSingleMove("e8","f8"));
            game.executeMove(WHITE, getSingleMove("f1","e1"));
            game.executeMove(BLACK, getSingleMove("f8","e8"));

            //Act
            game.executeMove(WHITE, new CastlingMove(getSingleMove("e1","g1"),
                    getSingleMove("h1","f1")));
            game.executeMove(BLACK, new CastlingMove(getSingleMove("e8","g8"),
                    getSingleMove("h8","f8")));

        });

        String expectedMessage = "Not allowed";
        String actualMessage = exception.getMessage();

        //Assert
        Assertions.assertTrue(actualMessage.contains(expectedMessage), "King has moved before");
    }

    @ParameterizedTest
    @CsvSource
            ({
                    "e1, c1, a1, d1, white",
                    "e1, g1, h1, f1, white",
                    "e8, c8, a8, d8, black",
                    "e8, g8, h8, f8, black"
            })
    public void serializedCastlingTest(AlgebraicSquare king_from, AlgebraicSquare king_to,
                                       AlgebraicSquare rook_from, AlgebraicSquare rook_to,
                                       String firstToMove)
            throws BoardException, MoveException, GameException {

        //Arrange
        Piece[] squares = new Piece[128];
        squares[toSquare("a1").value()] = new Piece(101, ROOK, WHITE);
        squares[toSquare("e1").value()] = new Piece(102, KING, WHITE);
        squares[toSquare("h1").value()] = new Piece(103, ROOK, WHITE);
        squares[toSquare("a8").value()] = new Piece(104, ROOK, BLACK);
        squares[toSquare("e8").value()] = new Piece(105, KING, BLACK);
        squares[toSquare("h8").value()] = new Piece(106, ROOK, BLACK);
        List<Integer> originalRooks = Arrays.asList(101, 103, 104, 106);
        board = new Board(squares, 107);

        Game game = new Game("", 1,
                new GameBoardContext(WHITE, board, null, new ArrayList<>(), originalRooks),
                GameStatus.active(), Color.fromString(firstToMove), false, new ArrayList<>());

        //Act
        game.executeMove(Color.fromString(firstToMove), getCastlingMove(king_from, king_to, rook_from, rook_to));
        Board board = game.getGameBoardContext().getCopyOfBoard();

        //Assert
        assertThat(board.getPieceAt(king_to.toSquare()).type()).isEqualTo(KING);
        assertThat(board.getPieceAt(rook_to.toSquare()).type()).isEqualTo(ROOK);
        assertThat(board.getPieceAt(king_from.toSquare())).isEqualTo(null);
        assertThat(board.getPieceAt(rook_from.toSquare())).isEqualTo(null);
    }
}
