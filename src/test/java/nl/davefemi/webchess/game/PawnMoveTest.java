package nl.davefemi.webchess.game;

import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.move.EnPassantMove;
import nl.davefemi.webchess.game.actions.move.PromotionMove;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.actions.record.EnPassantMoveRecord;
import nl.davefemi.webchess.game.actions.record.SingleMoveRecord;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.rule.RuleEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.*;
import static org.assertj.core.api.Assertions.*;


@Slf4j
public class PawnMoveTest {
    private Game game;
    private Board board;


    @BeforeEach
    public void setUp() throws GameException {
        game = new Game();
        game.start();
    }

    private SingleMove getSingleMove(String from, String to){
        return new SingleMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }

    private EnPassantMove getEnPassantMove(String from, String to){
        return new EnPassantMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }

    private Square toSquare(String position){
        return new AlgebraicSquare(position).toSquare();
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
        assertThat(RuleEngine.isMoveAllowed(game.getGameBoardContext(), game.getMoveHistory(), PieceColor.WHITE, getSingleMove(from, to))).isEqualTo(expected);
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

        Piece newPiece = game.getGameBoardContext().getCopyOfBoard().getPieceAt(new AlgebraicSquare("d8").toSquare());

        assertTrue(newPiece.type() == PieceType.QUEEN &&
                newPiece.color() == PieceColor.WHITE, "White piece has been promoted to type queen");

    }

    @Test
    public void promotionWithIllegalMoveTypeTest() throws MoveException, BoardException, GameException {
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
        //Arrange
        setUpPromotionMoves(game);

        //Act
        game.executeMove(PieceColor.WHITE, getSingleMove("b6","c7"));
        game.executeMove(PieceColor.BLACK, getSingleMove("a7","a6"));
        game.executeMove(PieceColor.WHITE, new PromotionMove(getSingleMove("c7","d8"), PieceType.QUEEN));

        //Assert
        assertEquals(PieceType.QUEEN, game.getGameBoardContext().getCopyOfBoard().getPieceAt(new AlgebraicSquare("d8").toSquare()).type(), "Queen is in 4,8");
        assertEquals(PieceColor.WHITE, game.getGameBoardContext().getCopyOfBoard().getPieceAt(new AlgebraicSquare("d8").toSquare()).color(), "Queen is WHITE");
        assertTrue("King is check", game.isPlayerInCheck(PieceColor.BLACK));


        assertEquals(30, game.getGameBoardContext().getCopyOfBoard().piecesOnBoard(), "Number of pieces");
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

    @Test
    public void enPassantTest2() throws MoveException, BoardException, GameException {
        game.executeMove(PieceColor.WHITE, getSingleMove("d2","d4"));
        game.executeMove(PieceColor.BLACK, getSingleMove("f7","f5"));
        game.executeMove(PieceColor.WHITE, getSingleMove("d4","d5"));
        game.executeMove(PieceColor.BLACK, getSingleMove("c7","c5"));
        game.executeMove(PieceColor.WHITE, getEnPassantMove("d5","c6"));
        assertThat(game.getMoveHistory().getLast() instanceof SingleMoveRecord);
         EnPassantMoveRecord record = (EnPassantMoveRecord) game.getMoveHistory().getLast();
        assertThat(record.capturedPiece() == PieceType.PAWN);

        log.info("Pawn with id: " + record.capturedPieceId() + " was captured");
    }

    @ParameterizedTest
    @CsvSource
            ({
                    "a2, b5, a7, a5, b5, a6, white",
                    "b2, c5, b7, b5, c5, b6, white",
                    "c2, d5, c7, c5, d5, c6, white",
                    "d2, e5, d7, d5, e5, d6, white",
                    "e2, f5, e7, e5, f5, e6, white",
                    "f2, g5, f7, f5, g5, f6, white",
                    "g2, h5, g7, g5, h5, g6, white",
                    "a7, b4, a2, a4, b4, a3, black",
                    "b7, c4, b2, b4, c4, b3, black",
                    "c7, d4, c2, c4, d4, c3, black",
                    "d7, e4, d2, d4, e4, d3, black",
                    "e7, f4, e2, e4, f4, e3, black",
                    "f7, g4, f2, f4, g4, f3, black",
                    "g7, h4, g2, g4, h4, g3, black",
                    "a2, a5, b7, b5, a5, b6, white",
                    "b2, b5, c7, c5, b5, c6, white",
                    "c2, c5, d7, d5, c5, d6, white",
                    "d2, d5, e7, e5, d5, e6, white",
                    "e2, e5, f7, f5, e5, f6, white",
                    "f2, f5, g7, g5, f5, g6, white",
                    "g2, g5, h7, h5, g5, h6, white",
                    "a7, a4, b2, b4, a4, b3, black",
                    "b7, b4, c2, c4, b4, c3, black",
                    "c7, c4, d2, d4, c4, d3, black",
                    "d7, d4, e2, e4, d4, e3, black",
                    "e7, e4, f2, f4, e4, f3, black",
                    "f7, f4, g2, g4, f4, g3, black",
                    "g7, g4, h2, h4, g4, h3, black"
            })
    public void enPassantTest3(String s1from, String s1to, String s2from, String s2to, String from, String to, String firstToMove) throws BoardException, MoveException, GameException {
        //Arrange
        GameBoardContext gameBoardContext = game.getGameBoardContext();
        gameBoardContext = gameBoardContext.applyValidatedMove(getSingleMove(s1from, s1to));
        gameBoardContext = gameBoardContext.applyValidatedMove(getSingleMove(s2from, s2to));
        //Act
        Game game = new Game(gameBoardContext, GameStatus.active(), PieceColor.fromString(firstToMove), new ArrayList<>());
        game.executeMove(PieceColor.fromString(firstToMove), getEnPassantMove(from, to));
        EnPassantMoveRecord record = (EnPassantMoveRecord) game.getMoveHistory().getLast();
        //Assert
        assertThat(record.capturedPiece()).isEqualTo(PieceType.PAWN);
        assertThat(game.getGameBoardContext().getCapturedPieces().getLast().color()).
                isEqualTo(PieceColor.getOpponent(PieceColor.fromString(firstToMove)));

    }
}
