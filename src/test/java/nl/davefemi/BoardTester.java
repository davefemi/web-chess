package nl.davefemi;

import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.board.BoardScanner;
import nl.davefemi.domain.board.Position;
import nl.davefemi.domain.game.Game;
import nl.davefemi.domain.game.PieceIdGenerator;
import nl.davefemi.domain.game.actions.move.CastlingMove;
import nl.davefemi.domain.game.actions.move.PromotionMove;
import nl.davefemi.domain.game.actions.move.SingleMove;
import nl.davefemi.domain.game.rule.MoveEvaluator;
import nl.davefemi.domain.board.PieceType;
import nl.davefemi.domain.board.PlayerColor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.MoveException;
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
        board = new Board(new PieceIdGenerator());

    }

    @AfterEach
    public void tearDown(){
    }

    private SingleMove getSingleMove(int file_start, int rank_start, int file_end, int rank_end){
        return new SingleMove(new Position(file_start, rank_start),new Position(file_end, rank_end));
    }

    @Test
    public void kingPresentCheckTest() throws MoveException, BoardException, GameException {
        assertTrue("Succeeded", game.executeMove(PlayerColor.WHITE, getSingleMove(2,2,2,4)));
    }


    @Test
    public void testCheck() throws BoardException {
        //Arrange
        board.movePieceTo(getSingleMove(4,1,4,8));

        //Act
        board.movePieceTo(new PromotionMove(new Position(4,8), "queen"));

        //Assert
        assertEquals(PieceType.QUEEN, board.getPieceAt(new Position(4,8)).getType(), "Queen is in 4,8");
        assertEquals(PlayerColor.WHITE, board.getPieceAt(new Position(4,8)).getColor(), "Queen is WHITE");
        assertTrue("King is check", MoveEvaluator
                .isKingCheck(
                        board,
                        BoardScanner.getCurrentSinglePosition(
                                board,
                                PieceType.KING,
                                PlayerColor.BLACK),
                        PlayerColor.WHITE));

    }

    @Test
    public void checkmateTest() throws MoveException, BoardException, GameException {
        //Arrange
        game.executeMove(PlayerColor.WHITE, getSingleMove(5,2,5,3));
        game.executeMove(PlayerColor.BLACK, getSingleMove(1,7,1,6));
        game.executeMove(PlayerColor.WHITE, getSingleMove(4,1,8,5));
        game.executeMove(PlayerColor.BLACK, getSingleMove(1,6,1,5));
        game.executeMove(PlayerColor.WHITE, getSingleMove(6,1,3,4));
        game.executeMove(PlayerColor.BLACK, getSingleMove(1,5,1,4));

        //Act
        game.executeMove(PlayerColor.WHITE, getSingleMove(8,5,6,7));

        //Assert
        assertTrue("King is check", game.isCheck(PlayerColor.BLACK));
        assertTrue("King is check-mate", game.isCheckMate(PlayerColor.BLACK));
    }

    @Test
    public void castlingTest1() throws MoveException, BoardException, GameException {
        //Arrange
        game.executeMove(PlayerColor.WHITE, getSingleMove(5,2,5,3));
        game.executeMove(PlayerColor.BLACK, getSingleMove(5,7,5,6));
        game.executeMove(PlayerColor.WHITE, getSingleMove(6,1,4,3));
        game.executeMove(PlayerColor.BLACK, getSingleMove(6,8,4,6));
        game.executeMove(PlayerColor.WHITE, getSingleMove(7,1,8,3));
        game.executeMove(PlayerColor.BLACK, getSingleMove(7,8,8,6));

        //Act
        game.executeMove(PlayerColor.WHITE, new CastlingMove(getSingleMove(5,1, 7,1),
                getSingleMove(8,1, 6,1)));
        game.executeMove(PlayerColor.BLACK, new CastlingMove(getSingleMove(5,8, 7,8),
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
            game.executeMove(PlayerColor.WHITE, getSingleMove(5,2,5,3));
            game.executeMove(PlayerColor.BLACK, getSingleMove(5,7,5,6));
            game.executeMove(PlayerColor.WHITE, getSingleMove(6,1,4,3));
            game.executeMove(PlayerColor.BLACK, getSingleMove(6,8,4,6));
            game.executeMove(PlayerColor.WHITE, getSingleMove(7,1,8,3));
            game.executeMove(PlayerColor.BLACK, getSingleMove(7,8,8,6));
            game.executeMove(PlayerColor.WHITE, getSingleMove(5,1,6,1));
            game.executeMove(PlayerColor.BLACK, getSingleMove(5,8,6,8));
            game.executeMove(PlayerColor.WHITE, getSingleMove(6,1,5,1));
            game.executeMove(PlayerColor.BLACK, getSingleMove(6,8,5,8));

            //Act
            game.executeMove(PlayerColor.WHITE, new CastlingMove(getSingleMove(5,1, 7,1),
                    getSingleMove(8,1, 6,1)));
            game.executeMove(PlayerColor.BLACK, new CastlingMove(getSingleMove(5,8, 7,8),
                    getSingleMove(8,8, 6,8)));

        });

        String expectedMessage = "Not allowed";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage), "King has moved before");


    }
}
