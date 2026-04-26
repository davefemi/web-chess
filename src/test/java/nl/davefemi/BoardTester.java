package nl.davefemi;

import nl.davefemi.game.board.Board;
import nl.davefemi.game.board.BoardScanner;
import nl.davefemi.game.board.Position;
import nl.davefemi.game.Game;
import nl.davefemi.game.IdGenerator;
import nl.davefemi.game.actions.CastlingMove;
import nl.davefemi.game.actions.PromotionMove;
import nl.davefemi.game.actions.SingleMove;
import nl.davefemi.game.rule.MoveEvaluator;
import nl.davefemi.game.board.PieceType;
import nl.davefemi.game.board.PieceColor;
import nl.davefemi.game.rule.Status;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.MoveException;
import nl.davefemi.session.AccessCode;
import nl.davefemi.session.AccessCodeGenerator;
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
    public void testCheck() throws BoardException {
        //Arrange
        board.applyValidatedMove(getSingleMove(4,1,4,8));

        //Act
        board.applyValidatedMove(new IdGenerator(), new PromotionMove(new Position(4,8), PieceType.QUEEN));

        //Assert
        assertEquals(PieceType.QUEEN, board.getPieceAt(new Position(4,8)).getType(), "Queen is in 4,8");
        assertEquals(PieceColor.WHITE, board.getPieceAt(new Position(4,8)).getColor(), "Queen is WHITE");
        assertTrue("King is check", MoveEvaluator
                .isKingCheck(
                        board,
                        BoardScanner.getCurrentSinglePiecePosition(
                                board,
                                PieceType.KING,
                                PieceColor.BLACK),
                        PieceColor.WHITE));

        assertEquals(31, board.piecesOnBoard(), "Number of pieces");

        AccessCode code = AccessCodeGenerator.getAccessCode("",60);
        System.out.println(code);
        System.out.println(code.getExpiresAt());

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
