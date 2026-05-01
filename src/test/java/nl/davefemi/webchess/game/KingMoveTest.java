package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.CastlingMove;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.TreeMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;


public class KingMoveTest {
    private Game game;
    private Board board ;
    private final PieceColor white = PieceColor.WHITE;
    private final PieceColor black = PieceColor.BLACK;


    @BeforeEach
    public void setUp(){
        game = new Game();
        board = game.getCopyOfBoard();
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
        Assertions.assertTrue(actualMessage.contains(expectedMessage), "King has moved before");
    }

    @Test
    public void blackQueenCannotAttackWhiteKingTest() throws BoardException, MoveException, GameException {
        TreeMap<Position, Piece> positions = new TreeMap<>();
        for (int rank = 1; rank<9; rank++){
            for (int file = 1; file<9; file++){
                positions.put(new Position (file, rank), null);
            }
        }
        positions.put(new Position(1,8), new Piece(1, PieceType.ROOK, PieceColor.WHITE));
        positions.put(new Position(4,8), new Piece(2, PieceType.QUEEN, PieceColor.BLACK));
        positions.put(new Position(5,8), new Piece(3, PieceType.KING, PieceColor.BLACK));
        positions.put(new Position(5,2), new Piece(4, PieceType.KING, PieceColor.WHITE));
        positions.put(new Position(5,1), new Piece(5, PieceType.QUEEN, PieceColor.WHITE));

        board = new Board(positions);

        Game game = new Game(6, board, true, PieceColor.WHITE, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        game.executeMove(PieceColor.WHITE, getSingleMove(5,2, 4,2));

        assertFalse("White King is NOT in check", game.getStatus(PieceColor.WHITE) == GameStatus.CHECK);
    }
}
