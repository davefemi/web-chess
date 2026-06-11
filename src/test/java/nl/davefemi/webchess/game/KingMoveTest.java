package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static nl.davefemi.webchess.game.board.PieceColor.BLACK;
import static nl.davefemi.webchess.game.board.PieceColor.WHITE;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;


public class KingMoveTest {
    private Game game;
    private Board board ;
    private final PieceColor white = PieceColor.WHITE;
    private final PieceColor black = PieceColor.BLACK;

    @BeforeEach
    public void setUp() throws GameException {
        game = new Game();
        game.start();
        board = game.getGameBoardContext().getCopyOfBoard();
    }

    private SingleMove getSingleMove(String from, String to){
        return new SingleMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }

    private Square toSquare(String position){
        return new AlgebraicSquare(position).toSquare();
    }


    @Test
    public void checkTest() throws BoardException {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(1, PieceType.ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(2, PieceType.QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(3, PieceType.KING, BLACK);
        squares[toSquare("e6").value()] = new Piece(4, PieceType.KING, WHITE);
        squares[toSquare("e1").value()] = new Piece(5, PieceType.QUEEN, WHITE);

        board = new Board(squares, 6);

        Game game = new Game(new GameBoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), GameStatus.active(), WHITE, new ArrayList<>());

        assertThatThrownBy(
                ()->
                        game.executeMove(WHITE, getSingleMove("e6","e7")))
                .hasMessageContaining("Illegal");
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

    @Test
    public void pinnedPieceTest() throws BoardException {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(1, PieceType.ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(2, PieceType.QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(3, PieceType.KING, BLACK);
        squares[toSquare("e2").value()] = new Piece(4, PieceType.KING, WHITE);
        squares[toSquare("e1").value()] = new Piece(5, PieceType.QUEEN, WHITE);

        board = new Board(squares, 6);

        Game game = new Game(new GameBoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), GameStatus.active(), WHITE, new ArrayList<>());

        assertThatThrownBy(
                ()->
                        game.executeMove(WHITE, getSingleMove("d8","d2")))
                .hasMessageContaining("Illegal");
        assertThat(game.isPlayerInCheck(BLACK)).isFalse().as("Black king is NOT check");
    }

    @Test
    public void stalemateTest() throws BoardException, MoveException, GameException {
        Piece [] squares = new Piece[128];
        squares[toSquare("b2").value()] = new Piece(1, PieceType.PAWN, WHITE);
        squares[toSquare("b4").value()] = new Piece(2, PieceType.KNIGHT, WHITE);
        squares[toSquare("b6").value()] = new Piece(3, PieceType.BISHOP, WHITE);
        squares[toSquare("d6").value()] = new Piece(4, PieceType.BISHOP, WHITE);
        squares[toSquare("d4").value()] = new Piece(5, PieceType.KNIGHT, WHITE);
        squares[toSquare("d2").value()] = new Piece(6, PieceType.PAWN, WHITE);
        squares[toSquare("c4").value()] = new Piece(7, PieceType.KING, BLACK);
        squares[toSquare("b1").value()] = new Piece(8, PieceType.KING, WHITE);

        board = new Board(squares, 9);

        Game game = new Game(new GameBoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), GameStatus.active(), WHITE, new ArrayList<>());
        game.executeMove(WHITE, getSingleMove("b1", "c1"));

        assertThat(game.getStatus()).isEqualTo(GameStatus.stalemate());
    }

    @Test
    public void kingInvariantTest() throws BoardException {
        Piece [] squares = new Piece[128];
        squares[toSquare("d3").value()] = new Piece(1, PieceType.PAWN, WHITE);
        squares[toSquare("b4").value()] = new Piece(2, PieceType.KNIGHT, WHITE);
        squares[toSquare("b6").value()] = new Piece(3, PieceType.BISHOP, WHITE);
        squares[toSquare("d6").value()] = new Piece(4, PieceType.BISHOP, WHITE);
        squares[toSquare("d4").value()] = new Piece(5, PieceType.KNIGHT, WHITE);
        squares[toSquare("d2").value()] = new Piece(6, PieceType.PAWN, WHITE);
        squares[toSquare("c4").value()] = new Piece(7, PieceType.KING, BLACK);
        squares[toSquare("b1").value()] = new Piece(8, PieceType.KING, WHITE);

        board = new Board(squares, 9);

        Game game = new Game(new GameBoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), GameStatus.active(), WHITE, new ArrayList<>());
        assertThatException().isThrownBy(()->
            game.executeMove(WHITE, getSingleMove("d3", "d4"))
        ).withMessageContaining("Board must have exactly one king of each colour");

        assertThat(game.getStatus()).isEqualTo(GameStatus.active());
    }
}
