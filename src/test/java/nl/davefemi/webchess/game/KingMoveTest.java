package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.springframework.test.util.AssertionErrors.assertTrue;


public class KingMoveTest {
    private Game game;
    private Board board ;
    private final PieceColor white = PieceColor.WHITE;
    private final PieceColor black = PieceColor.BLACK;


    @BeforeEach
    public void setUp() {
        game = new Game();
        board = game.getCurrentBoardContext().getCopyOfBoard();
    }

    private SingleMove getSingleMove(int file_start, int rank_start, int file_end, int rank_end){
        return new SingleMove(new Position(file_start, rank_start),new Position(file_end, rank_end));
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
        assertTrue("King is check-mate", game.getStatus(PieceColor.BLACK) == GameStatus.CHECKMATE);
        assertTrue("White player is the winner", game.getStatus(PieceColor.WHITE) == GameStatus.WINNER);
    }
}
