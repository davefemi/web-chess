package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.move.CastlingMove;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertFalse;


public class CastlingMoveTest {
    private Game game;
    private Board board ;
    private static final PieceColor WHITE = PieceColor.WHITE;
    private static final PieceColor BLACK = PieceColor.BLACK;


    @BeforeEach
    public void setUp() {
        game = new Game();
        board = game.getCurrentBoardContext().getCopyOfBoard();
    }

    private SingleMove getSingleMove(String from, String to){
        return new SingleMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }

    private Square toSquare(String position){
        return new AlgebraicSquare(position).toSquare();
    }


    @Test
    public void castlingTest1() throws MoveException, BoardException, GameException {
        //Arrange
        game.executeMove(WHITE, getSingleMove("e2","e3"));
        game.executeMove(BLACK, getSingleMove("e7","e6"));
        game.executeMove(WHITE, getSingleMove("f1","d3"));
        game.executeMove(BLACK, getSingleMove("f8","d6"));
        game.executeMove(WHITE, getSingleMove("g1","h3"));
        game.executeMove(BLACK, getSingleMove("g8","h6"));

        //Act
        game.executeMove(WHITE, new CastlingMove(getSingleMove("e1","g1"),
                getSingleMove("h1", "f1")));
        game.executeMove(BLACK, new CastlingMove(getSingleMove("e8","g8"),
                getSingleMove("h8","f8")));

        //Assert
        assertEquals(PieceType.KING, game.getCurrentBoardContext().getCopyOfBoard().getPieceAt(toSquare("g1")).getType(),"White king has castled");
        assertEquals(PieceType.ROOK, game.getCurrentBoardContext().getCopyOfBoard().getPieceAt(toSquare("f1")).getType(),"White rook has castled");
        assertEquals(PieceType.KING, game.getCurrentBoardContext().getCopyOfBoard().getPieceAt(toSquare("g8")).getType(),"Black king has castled");
        assertEquals(PieceType.ROOK, game.getCurrentBoardContext().getCopyOfBoard().getPieceAt(toSquare("f8")).getType(),"Black rook has castled");
    }

    @Test
    public void castlingTest2(){
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
            game.executeMove(PieceColor.BLACK, new CastlingMove(getSingleMove("e8","g8"),
                    getSingleMove("h8","f8")));

        });

        String expectedMessage = "Not allowed";
        String actualMessage = exception.getMessage();

        //Assert
        Assertions.assertTrue(actualMessage.contains(expectedMessage), "King has moved before");
    }

    @Test
    public void blackQueenCannotAttackWhiteKingTest() throws BoardException, MoveException, GameException {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(1, PieceType.ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(2, PieceType.QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(3, PieceType.KING, BLACK);
        squares[toSquare("e2").value()] = new Piece(4, PieceType.KING, WHITE);
        squares[toSquare("e1").value()] = new Piece(5, PieceType.QUEEN, WHITE);

        board = new Board(squares, 6);

        Game game = new Game(new BoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), GameStatus.active(), PieceColor.WHITE, new ArrayList<>());

        game.executeMove(WHITE, getSingleMove("e2","d2"));

        assertFalse("White King is NOT in check", game.isPlayerInCheck(WHITE));
    }
}
