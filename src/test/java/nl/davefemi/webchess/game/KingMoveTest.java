package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.move.SingleMove;
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

    private SingleMove getSingleMove(String from, String to){
        return new SingleMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }

    @Test
    public void checkmateTest() throws MoveException, BoardException, GameException {
        //Arrange
        game.executeMove(PieceColor.WHITE, getSingleMove("e2","e3"));
        game.executeMove(PieceColor.BLACK, getSingleMove("a7","a6"));
        game.executeMove(PieceColor.WHITE, getSingleMove("d1","h5"));
        game.executeMove(PieceColor.BLACK, getSingleMove("a6","a5"));
        game.executeMove(PieceColor.WHITE, getSingleMove("f1","c4"));
        game.executeMove(PieceColor.BLACK, getSingleMove("a5","a4"));

        //Act
        game.executeMove(PieceColor.WHITE, getSingleMove("h5","f7"));

        //Assert
        assertTrue("King is check-mate", game.getStatus().reason().isPresent() &&
                game.getStatus().reason().get() == GameStatus.GameEndReason.CHECKMATE);
        assertTrue("White player is the winner", game.getStatus().winner().isPresent() &&
                game.getStatus().winner().get() == PieceColor.WHITE);
    }
}
