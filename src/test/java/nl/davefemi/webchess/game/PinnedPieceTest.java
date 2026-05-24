package nl.davefemi.webchess.game;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.SingleMove;
import nl.davefemi.webchess.game.board.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.TreeMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PinnedPieceTest {
    private Game game;
    private Board board ;
    private final PieceColor white = PieceColor.WHITE;
    private final PieceColor black = PieceColor.BLACK;

    @BeforeEach
    public void setUp(){
        game = new Game();
        board = game.getCurrentBoardContext().getCopyOfBoard();
    }

    private SingleMove getSingleMove(int file_start, int rank_start, int file_end, int rank_end){
        return new SingleMove(new Position(file_start, rank_start),new Position(file_end, rank_end));
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

        board = new Board(positions, 6);

        Game game = new Game(new BoardContext(PieceColor.WHITE, board, null, new ArrayList<>(), new ArrayList<>()), true, PieceColor.WHITE, new ArrayList<>());

        game.executeMove(PieceColor.WHITE, getSingleMove(5,2, 4,2));

        assertThat(game.getStatus(PieceColor.WHITE)).isEqualByComparingTo(GameStatus.ACTIVE).as("White King is NOT in check");
        assertThat(game.getStatus(PieceColor.BLACK)).isEqualTo(GameStatus.CHECK).as("Black king is check");
        assertThatThrownBy(
                ()->
                        game.executeMove(black, getSingleMove(4,8, 4,4)))
                .hasMessage("Illegal");
    }
}
