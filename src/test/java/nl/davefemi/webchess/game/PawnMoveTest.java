package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.EnPassantMove;
import nl.davefemi.webchess.game.actions.PromotionMove;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.rule.RuleEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.*;
import static org.assertj.core.api.Assertions.*;

public class PawnMoveTest {
    private Game game;


    @BeforeEach
    public void setUp(){
        game = new Game();
    }

    private SingleMove getSingleMove(String from, String to){
        return new SingleMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }

    private EnPassantMove getEnPassantMove(String from, String to){
        return new EnPassantMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }


    @ParameterizedTest
    @CsvSource({
            "b2,b4, true",
            "b2,b3, true",
            "b2,b1, false",
            "b2,c2, false",
            "b2,b5, false",
            "b2,b5, false"
    })
    public void startingMoveTest(String from, String to, boolean expected) throws MoveException, BoardException {
        assertThat(RuleEngine.isMoveAllowed(game.getCurrentBoardContext(), game.getMoveHistory(), PieceColor.WHITE, getSingleMove(from, to))).isEqualTo(expected);
    }

    @Test
    public void illegalPawnMoveTest() {
        Exception exception = assertThrows(MoveException.class, ()-> {
                    game.executeMove(PieceColor.WHITE, getSingleMove("b2", "b4"));
                    game.executeMove(PieceColor.BLACK, getSingleMove("a7", "a5"));
                    game.executeMove(PieceColor.WHITE, getSingleMove("h2", "h3"));
                    game.executeMove(PieceColor.BLACK, getSingleMove("a5", "a4"));
                    game.executeMove(PieceColor.WHITE, getSingleMove("h3", "h4"));
                    game.executeMove(PieceColor.BLACK, getSingleMove("a4", "a3"));
                    game.executeMove(PieceColor.WHITE, getSingleMove("a2", "a4"));
                }
        );

        String expectedMessage = "Illegal move";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage), "Pawn jump is disallowed");
    }


    private void setUpPromotionMoves(Game game) throws MoveException, BoardException, GameException {
        game.executeMove(PieceColor.WHITE, getSingleMove("b2","b4"));
        game.executeMove(PieceColor.BLACK, getSingleMove("g7","g5"));
        game.executeMove(PieceColor.WHITE, getSingleMove("b4","b5"));
        game.executeMove(PieceColor.BLACK, getSingleMove("g5","g4"));
        game.executeMove(PieceColor.WHITE, getSingleMove("b5","b6"));
        game.executeMove(PieceColor.BLACK, getSingleMove("g4","g3"));
    }

    @Test
    public void promotionFromIllegalPositionTest() throws MoveException, BoardException, GameException {
        setUpPromotionMoves(game);
        Exception exception = assertThrows(MoveException.class, () ->
            game.executeMove(PieceColor.WHITE,
                    new PromotionMove(getSingleMove("b6","c7"), PieceType.QUEEN)));

        String expectedMessage = "up for promotion";
        String actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage), "Pawn is not up for promotion");

        game.executeMove(PieceColor.WHITE, getSingleMove("b6","c7"));

        exception = assertThrows(MoveException.class, () ->
            game.executeMove(PieceColor.BLACK,
                    new PromotionMove(getSingleMove("g3","h2"), PieceType.QUEEN)));

        expectedMessage = "up for promotion";
        actualMessage = exception.getMessage();

        //Assert
        assertTrue(actualMessage.contains(expectedMessage), "Pawn is not up for promotion");


        game.executeMove(PieceColor.BLACK, getSingleMove("g3","h2"));
        game.executeMove(PieceColor.WHITE, new PromotionMove(getSingleMove("c7","d8"), PieceType.QUEEN));

        Piece newPiece = game.getCurrentBoardContext().getCopyOfBoard().getPieceAt(new AlgebraicSquare("d8").toSquare());

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
            game.executeMove(PieceColor.WHITE, getSingleMove("b6","c7"));
            game.executeMove(PieceColor.BLACK, getSingleMove("a7","a6"));
            game.executeMove(PieceColor.WHITE, getSingleMove("c7","d8"));
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
        game.executeMove(PieceColor.WHITE, getSingleMove("b6","c7"));
        game.executeMove(PieceColor.BLACK, getSingleMove("a7","a6"));
        game.executeMove(PieceColor.WHITE, new PromotionMove(getSingleMove("c7","d8"), PieceType.QUEEN));

        //Assert
        assertEquals(PieceType.QUEEN, game.getCurrentBoardContext().getCopyOfBoard().getPieceAt(new AlgebraicSquare("d8").toSquare()).getType(), "Queen is in 4,8");
        assertEquals(PieceColor.WHITE, game.getCurrentBoardContext().getCopyOfBoard().getPieceAt(new AlgebraicSquare("d8").toSquare()).getColor(), "Queen is WHITE");
        assertTrue("King is check", game.getStatus(PieceColor.BLACK) == GameStatus.CHECK);


        assertEquals(30, game.getCurrentBoardContext().getCopyOfBoard().piecesOnBoard(), "Number of pieces");
    }

    @Test
    public void enPassantTest() throws MoveException, BoardException, GameException {
        game.executeMove(PieceColor.WHITE, getSingleMove("a2","a3"));
        game.executeMove(PieceColor.BLACK, getSingleMove("f7","f5"));
        game.executeMove(PieceColor.WHITE, getSingleMove("a3","a4"));
        game.executeMove(PieceColor.BLACK, getSingleMove("f5","f4"));
        game.executeMove(PieceColor.WHITE, getSingleMove("g2","g4"));
        game.executeMove(PieceColor.BLACK, getEnPassantMove("f4","g3"));
    }
}
