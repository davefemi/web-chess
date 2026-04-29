package nl.davefemi.webchess;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.IdGenerator;
import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.rule.MoveEvaluator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.*;



@SpringBootTest
public class PawnMoveTest {
    private Game game;
    private Board board;


    @BeforeEach
    public void setUp(){
        game = new Game();
        board = new Board(new IdGenerator());
    }

    private SingleMove getSingleMove(int file_start, int rank_start, int file_end, int rank_end){
        return new SingleMove(new Position(file_start, rank_start),new Position(file_end, rank_end));
    }


    @Test
    public void illegalPawnMoveTest() {
        Exception exception = assertThrows(MoveException.class, ()-> {
                    game.executeMove(PieceColor.WHITE, getSingleMove(2, 2, 2, 4));
                    game.executeMove(PieceColor.BLACK, getSingleMove(1, 7, 1, 5));
                    game.executeMove(PieceColor.WHITE, getSingleMove(8, 2, 8, 3));
                    game.executeMove(PieceColor.BLACK, getSingleMove(1, 5, 1, 4));
                    game.executeMove(PieceColor.WHITE, getSingleMove(8, 3, 8, 4));
                    game.executeMove(PieceColor.BLACK, getSingleMove(1, 4, 1, 3));
                    game.executeMove(PieceColor.WHITE, getSingleMove(1, 2, 1, 4));
                }
        );

        String expectedMessage = "Illegal move";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage), "Pawn jump is disallowed");
    }


    private void setUpPromotionMoves(Game game) throws MoveException, BoardException, GameException {
        game.executeMove(PieceColor.WHITE, getSingleMove(2,2,2,4));
        game.executeMove(PieceColor.BLACK, getSingleMove(8,7,8,5));
        game.executeMove(PieceColor.WHITE, getSingleMove(2,4,2,5));
        game.executeMove(PieceColor.BLACK, getSingleMove(8,5,8,4));
        game.executeMove(PieceColor.WHITE, getSingleMove(2,5,2,6));
        game.executeMove(PieceColor.BLACK, getSingleMove(8,4,8,3));
    }

    @Test
    public void promotionFromIllegalPositionTest() throws MoveException, BoardException, GameException {
        setUpPromotionMoves(game);
        Exception exception = assertThrows(MoveException.class, () ->
            game.executeMove(PieceColor.WHITE,
                    new PromotionMove(getSingleMove(2,6,3, 7), PieceType.QUEEN)));

        String expectedMessage = "up for promotion";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage), "Pawn is not up for promotion");

        game.executeMove(PieceColor.WHITE, getSingleMove(2,6,3,7));

        exception = assertThrows(MoveException.class, () ->
            game.executeMove(PieceColor.BLACK,
                    new PromotionMove(getSingleMove(8,3,7, 2), PieceType.QUEEN)));

        expectedMessage = "up for promotion";
        actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage), "Pawn is not up for promotion");


        game.executeMove(PieceColor.BLACK, getSingleMove(8,3,7,2));
        game.executeMove(PieceColor.WHITE, new PromotionMove(getSingleMove(3,7,4, 8), PieceType.QUEEN));

        Piece newPiece = game.getCopyOfBoard().getPieceAt(new Position(4,8));

        assertTrue(newPiece.getType() == PieceType.QUEEN &&
                newPiece.getColor() == PieceColor.WHITE, "White piece has been promoted to type queen");

    }

    @Test
    public void promotionWithIllegalMoveTypeTest() throws MoveException, BoardException, GameException {
        Game game = new Game();
        //Arrange
        setUpPromotionMoves(game);

        //Act
        Exception exception = assertThrows(MoveException.class, ()-> {
            game.executeMove(PieceColor.WHITE, getSingleMove(2,6,3,7));
            game.executeMove(PieceColor.BLACK, getSingleMove(1,7,1,6));
            game.executeMove(PieceColor.WHITE, getSingleMove(3,7,4,8));
        });

        String expectedMessage = "Single move not allowed here";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage), "Single move for white not allowed from rank 7");
    }

    @Test
    public void successfulPromotionTest() throws BoardException, MoveException, GameException {
        Game game = new Game();
        //Arrange
        setUpPromotionMoves(game);

        //Act
        game.executeMove(PieceColor.WHITE, getSingleMove(2,6,3,7));
        game.executeMove(PieceColor.BLACK, getSingleMove(1,7,1,6));
        game.executeMove(PieceColor.WHITE, new PromotionMove(getSingleMove(3,7,4, 8), PieceType.QUEEN));

        //Assert
        assertEquals(PieceType.QUEEN, game.getCopyOfBoard().getPieceAt(new Position(4,8)).getType(), "Queen is in 4,8");
        assertEquals(PieceColor.WHITE, game.getCopyOfBoard().getPieceAt(new Position(4,8)).getColor(), "Queen is WHITE");
        assertTrue("King is check", MoveEvaluator
                .isKingInCheck(
                        game.getCopyOfBoard(), null,
                        BoardScanner.getCurrentSinglePiecePosition(
                                board,
                                PieceType.KING,
                                PieceColor.BLACK),
                        PieceColor.WHITE));

        assertEquals(30, game.getCopyOfBoard().piecesOnBoard(), "Number of pieces");
    }
}
