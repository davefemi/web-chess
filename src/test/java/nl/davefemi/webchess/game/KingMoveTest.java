package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static nl.davefemi.webchess.game.Color.BLACK;
import static nl.davefemi.webchess.game.Color.WHITE;
import static nl.davefemi.webchess.game.board.PieceType.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;


public class KingMoveTest {
    private Game game;
    private Board board ;

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
        squares[toSquare("a8").value()] = new Piece(101, ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(102, QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(103, KING, BLACK);
        squares[toSquare("e6").value()] = new Piece(104, KING, WHITE);
        squares[toSquare("e1").value()] = new Piece(105, QUEEN, WHITE);

        board = new Board(squares, 106);

        Game game = new Game(1, new GameBoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), GameStatus.active(), WHITE, false, new ArrayList<>());

        assertThatThrownBy(
                ()->
                        game.executeMove(WHITE, getSingleMove("e6","e7")))
                .hasMessageContaining("Illegal");
    }

    @Test
    public void checkmateTest() throws MoveException, BoardException, GameException {
        //Arrange
        game.executeMove(WHITE, getSingleMove("e2","e3"));
        game.executeMove(BLACK, getSingleMove("a7","a6"));
        game.executeMove(WHITE, getSingleMove("d1","h5"));
        game.executeMove(BLACK, getSingleMove("a6","a5"));
        game.executeMove(WHITE, getSingleMove("f1","c4"));
        game.executeMove(BLACK, getSingleMove("a5","a4"));

        //Act
        game.executeMove(WHITE, getSingleMove("h5","f7"));

        //Assert
        assertTrue("King is check-mate", game.getStatus().reason().isPresent() &&
                game.getStatus().reason().get() == GameStatus.GameEndReason.CHECKMATE);
        assertTrue("White player is the winner", game.getStatus().winner().isPresent() &&
                game.getStatus().winner().get() == WHITE);
    }

    @Test
    public void pinnedPieceTest() throws BoardException {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(101, ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(102, QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(103, KING, BLACK);
        squares[toSquare("e2").value()] = new Piece(104, KING, WHITE);
        squares[toSquare("e1").value()] = new Piece(105, QUEEN, WHITE);

        board = new Board(squares, 106);

        Game game = new Game(1, new GameBoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), GameStatus.active(), WHITE, false, new ArrayList<>());

        assertThatThrownBy(
                ()->
                        game.executeMove(WHITE, getSingleMove("d8","d2")))
                .hasMessageContaining("Illegal");
        assertThat(game.isCheck(BLACK)).isFalse().as("Black king is NOT check");
    }

    @Test
    public void stalemateTest() throws BoardException, MoveException, GameException {
        Piece [] squares = new Piece[128];
        squares[toSquare("b2").value()] = new Piece(101, PAWN, WHITE);
        squares[toSquare("b4").value()] = new Piece(102, KNIGHT, WHITE);
        squares[toSquare("b6").value()] = new Piece(103, BISHOP, WHITE);
        squares[toSquare("d6").value()] = new Piece(104, BISHOP, WHITE);
        squares[toSquare("d4").value()] = new Piece(105, KNIGHT, WHITE);
        squares[toSquare("d2").value()] = new Piece(106, PAWN, WHITE);
        squares[toSquare("c4").value()] = new Piece(107, KING, BLACK);
        squares[toSquare("b1").value()] = new Piece(108, KING, WHITE);

        board = new Board(squares, 109);

        Game game = new Game(1, new GameBoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), GameStatus.active(), WHITE, false, new ArrayList<>());
        game.executeMove(WHITE, getSingleMove("b1", "c1"));

        assertThat(game.getStatus()).isEqualTo(GameStatus.stalemate());
    }

    @Test
    public void kingInvariantTest() throws BoardException {
        Piece [] squares = new Piece[128];
        squares[toSquare("d3").value()] = new Piece(101, PAWN, WHITE);
        squares[toSquare("b4").value()] = new Piece(102, KNIGHT, WHITE);
        squares[toSquare("b6").value()] = new Piece(103, BISHOP, WHITE);
        squares[toSquare("d6").value()] = new Piece(104, BISHOP, WHITE);
        squares[toSquare("d4").value()] = new Piece(105, KNIGHT, WHITE);
        squares[toSquare("d2").value()] = new Piece(106, PAWN, WHITE);
        squares[toSquare("c4").value()] = new Piece(107, KING, BLACK);
        squares[toSquare("b1").value()] = new Piece(108, KING, WHITE);

        board = new Board(squares, 109);

        Game game = new Game(1, new GameBoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), GameStatus.active(), WHITE, false, new ArrayList<>());
        assertThatException().isThrownBy(()->
            game.executeMove(WHITE, getSingleMove("d3", "d4"))
        ).withMessageContaining("Board must have exactly one king of each colour");

        assertThat(game.getStatus()).isEqualTo(GameStatus.active());
    }
}
