package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.move.SingleMove;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.rule.AttackDetector;
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
    public void blackQueenCannotAttackWhiteKingTest() throws BoardException, MoveException, GameException {
        Piece [] squares = new Piece[128];
        squares[toSquare("a8").value()] = new Piece(1, PieceType.ROOK, WHITE);
        squares[toSquare("d8").value()] = new Piece(2, PieceType.QUEEN, BLACK);
        squares[toSquare("e8").value()] = new Piece(3, PieceType.KING, BLACK);
        squares[toSquare("e2").value()] = new Piece(4, PieceType.KING, WHITE);
        squares[toSquare("e1").value()] = new Piece(5, PieceType.QUEEN, WHITE);

        board = new Board(squares, 6);

        Game game = new Game(new BoardContext(WHITE, board, null, new ArrayList<>(), new ArrayList<>()), GameStatus.active(), WHITE, new ArrayList<>());
        game.executeMove(WHITE, getSingleMove("e2","d2"));

        System.out.println("Position e8 under attack is: " + AttackDetector.detectAttack(game.getCurrentBoardContext().getCopyOfBoard(), new AlgebraicSquare("e8").toSquare(), BLACK));
        System.out.println("Position d2 under attack is: " + AttackDetector.detectAttack(game.getCurrentBoardContext().getCopyOfBoard(), new AlgebraicSquare("d2").toSquare(), WHITE));

        assertThat(game.isPlayerInCheck(WHITE)).isFalse().as("White King is NOT in check");
        assertThat(game.isPlayerInCheck(BLACK)).isTrue().as("Black king is check");
        assertThatThrownBy(
                ()->
                        game.executeMove(black, getSingleMove("d8","d4")))
                .hasMessage("Illegal");
    }
}
