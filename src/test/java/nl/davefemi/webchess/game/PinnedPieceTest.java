package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static nl.davefemi.webchess.game.board.PieceColor.BLACK;
import static nl.davefemi.webchess.game.board.PieceColor.WHITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PinnedPieceTest {
    private Game game;
    private Board board ;
    private final PieceColor white = WHITE;
    private final PieceColor black = BLACK;

    @BeforeEach
    public void setUp() throws GameException {
        game = new Game();
        game.start();
        board = game.getCurrentBoardContext().getCopyOfBoard();
    }

    private SingleMove getSingleMove(String from, String to){
        return new SingleMove(new AlgebraicSquare(from).toSquare(),new AlgebraicSquare(to).toSquare());
    }

    private Square toSquare(String position){
        return new AlgebraicSquare(position).toSquare();
    }

    @Test
    public void whiteQueenCannotAttackBlackKingTest() throws BoardException {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(1, PieceType.ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(2, PieceType.QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(3, PieceType.KING, BLACK);
        squares[toSquare("e2").value()] = new Piece(4, PieceType.KING, WHITE);
        squares[toSquare("e1").value()] = new Piece(5, PieceType.QUEEN, WHITE);

        board = new Board(squares, 6);

        Game game = new Game(new BoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), GameStatus.active(), WHITE, new ArrayList<>());

        assertThatThrownBy(
                ()->
                        game.executeMove(WHITE, getSingleMove("e2","d2")))
                .hasMessageContaining("Illegal");
        assertThat(game.isPlayerInCheck(BLACK)).isFalse().as("Black king is NOT check");
    }
}
