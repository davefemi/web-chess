package nl.davefemi.webchess;

import nl.davefemi.webchess.game.board.Board;
import nl.davefemi.webchess.game.board.Position;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.IdGenerator;
import nl.davefemi.webchess.game.actions.CastlingMove;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.PieceType;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.rule.Status;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.util.AssertionErrors.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BoardTester {
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

    @Test
    public void kingPresentCheckTest() throws MoveException, BoardException, GameException {
        assertTrue("Succeeded", game.executeMove(PieceColor.WHITE, getSingleMove(2,2,2,4)));
    }


    @Test
    public void checkmateTest() throws MoveException, BoardException, GameException {
        //Arrange
        game.executeMove(PieceColor.WHITE, getSingleMove(5,2,5,3));
        game.executeMove(PieceColor.BLACK, getSingleMove(1,7,1,6));
        game.executeMove(PieceColor.WHITE, getSingleMove(4,1,8,5));
        game.executeMove(PieceColor.BLACK, getSingleMove(1,6,1,5));
        game.executeMove(PieceColor.WHITE, getSingleMove(6,1,3,4));
        game.executeMove(PieceColor.BLACK, getSingleMove(1,5,1,4));

        //Act
        game.executeMove(PieceColor.WHITE, getSingleMove(8,5,6,7));

        //Assert
        assertTrue("King is check", game.getStatus(PieceColor.BLACK) == Status.CHECKMATE);
    }

    @Test
    public void castlingTest1() throws MoveException, BoardException, GameException {
        //Arrange
        game.executeMove(PieceColor.WHITE, getSingleMove(5,2,5,3));
        game.executeMove(PieceColor.BLACK, getSingleMove(5,7,5,6));
        game.executeMove(PieceColor.WHITE, getSingleMove(6,1,4,3));
        game.executeMove(PieceColor.BLACK, getSingleMove(6,8,4,6));
        game.executeMove(PieceColor.WHITE, getSingleMove(7,1,8,3));
        game.executeMove(PieceColor.BLACK, getSingleMove(7,8,8,6));

        //Act
        game.executeMove(PieceColor.WHITE, new CastlingMove(getSingleMove(5,1, 7,1),
                getSingleMove(8,1, 6,1)));
        game.executeMove(PieceColor.BLACK, new CastlingMove(getSingleMove(5,8, 7,8),
                getSingleMove(8,8, 6,8)));

        //Assert
        assertEquals(PieceType.KING, game.getCopyOfBoard().getPieceAt(new Position(7,1)).getType(),"White king has castled");
        assertEquals(PieceType.ROOK, game.getCopyOfBoard().getPieceAt(new Position(6,1)).getType(),"White rook has castled");
        assertEquals(PieceType.KING, game.getCopyOfBoard().getPieceAt(new Position(7,8)).getType(),"Black king has castled");
        assertEquals(PieceType.ROOK, game.getCopyOfBoard().getPieceAt(new Position(6,8)).getType(),"Black rook has castled");
    }

    @Test
    public void castlingTest2(){
        Exception exception = assertThrows(MoveException.class, () ->{
            //Arrange
            game.executeMove(PieceColor.WHITE, getSingleMove(5,2,5,3));
            game.executeMove(PieceColor.BLACK, getSingleMove(5,7,5,6));
            game.executeMove(PieceColor.WHITE, getSingleMove(6,1,4,3));
            game.executeMove(PieceColor.BLACK, getSingleMove(6,8,4,6));
            game.executeMove(PieceColor.WHITE, getSingleMove(7,1,8,3));
            game.executeMove(PieceColor.BLACK, getSingleMove(7,8,8,6));
            game.executeMove(PieceColor.WHITE, getSingleMove(5,1,6,1));
            game.executeMove(PieceColor.BLACK, getSingleMove(5,8,6,8));
            game.executeMove(PieceColor.WHITE, getSingleMove(6,1,5,1));
            game.executeMove(PieceColor.BLACK, getSingleMove(6,8,5,8));

            //Act
            game.executeMove(PieceColor.WHITE, new CastlingMove(getSingleMove(5,1, 7,1),
                    getSingleMove(8,1, 6,1)));
            game.executeMove(PieceColor.BLACK, new CastlingMove(getSingleMove(5,8, 7,8),
                    getSingleMove(8,8, 6,8)));

        });

        String expectedMessage = "Not allowed";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage), "King has moved before");


    }
}
