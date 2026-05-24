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

    private SingleMove getSingleMove(int file_start, int rank_start, int file_end, int rank_end){
        return new SingleMove(new Position(file_start, rank_start),new Position(file_end, rank_end));
    }


    @Test
    public void castlingTest1() throws MoveException, BoardException, GameException {
        //Arrange
        game.executeMove(WHITE, getSingleMove(5,2,5,3));
        game.executeMove(BLACK, getSingleMove(5,7,5,6));
        game.executeMove(WHITE, getSingleMove(6,1,4,3));
        game.executeMove(BLACK, getSingleMove(6,8,4,6));
        game.executeMove(WHITE, getSingleMove(7,1,8,3));
        game.executeMove(BLACK, getSingleMove(7,8,8,6));

        //Act
        game.executeMove(WHITE, new CastlingMove(getSingleMove(5,1, 7,1),
                getSingleMove(8,1, 6,1)));
        game.executeMove(BLACK, new CastlingMove(getSingleMove(5,8, 7,8),
                getSingleMove(8,8, 6,8)));

        //Assert
        assertEquals(PieceType.KING, game.getCurrentBoardContext().getCopyOfBoard().getPieceAt(new Position(7,1)).getType(),"White king has castled");
        assertEquals(PieceType.ROOK, game.getCurrentBoardContext().getCopyOfBoard().getPieceAt(new Position(6,1)).getType(),"White rook has castled");
        assertEquals(PieceType.KING, game.getCurrentBoardContext().getCopyOfBoard().getPieceAt(new Position(7,8)).getType(),"Black king has castled");
        assertEquals(PieceType.ROOK, game.getCurrentBoardContext().getCopyOfBoard().getPieceAt(new Position(6,8)).getType(),"Black rook has castled");
    }

    @Test
    public void castlingTest2(){
        Exception exception = assertThrows(MoveException.class, () ->{
            //Arrange
            game.executeMove(WHITE, getSingleMove(5,2,5,3));
            game.executeMove(BLACK, getSingleMove(5,7,5,6));
            game.executeMove(WHITE, getSingleMove(6,1,4,3));
            game.executeMove(BLACK, getSingleMove(6,8,4,6));
            game.executeMove(WHITE, getSingleMove(7,1,8,3));
            game.executeMove(BLACK, getSingleMove(7,8,8,6));
            game.executeMove(WHITE, getSingleMove(5,1,6,1));
            game.executeMove(BLACK, getSingleMove(5,8,6,8));
            game.executeMove(WHITE, getSingleMove(6,1,5,1));
            game.executeMove(BLACK, getSingleMove(6,8,5,8));

            //Act
            game.executeMove(WHITE, new CastlingMove(getSingleMove(5,1, 7,1),
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
        positions.put(new Position(1,8), new Piece(1, PieceType.ROOK, WHITE));
        positions.put(new Position(4,8), new Piece(2, PieceType.QUEEN, BLACK));
        positions.put(new Position(5,8), new Piece(3, PieceType.KING, BLACK));
        positions.put(new Position(5,2), new Piece(4, PieceType.KING, WHITE));
        positions.put(new Position(5,1), new Piece(5, PieceType.QUEEN, WHITE));

        board = new Board(positions, 6);

        Game game = new Game(new BoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), true, PieceColor.WHITE, new ArrayList<>());

        game.executeMove(WHITE, getSingleMove(5,2, 4,2));

        assertFalse("White King is NOT in check", game.getStatus(WHITE) == GameStatus.CHECK);
    }
}
