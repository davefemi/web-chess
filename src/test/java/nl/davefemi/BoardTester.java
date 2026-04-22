package nl.davefemi;

import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.board.BoardScanner;
import nl.davefemi.domain.board.Position;
import nl.davefemi.domain.game.move.PromotionMove;
import nl.davefemi.domain.game.move.SingleMove;
import nl.davefemi.domain.game.rule.MoveEvaluator;
import nl.davefemi.domain.piece.PieceType;
import nl.davefemi.domain.piece.PlayerColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.springframework.test.util.AssertionErrors.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BoardTester {
    private Board board;

    @BeforeEach
    public void setUp(){
        board = new Board();
    }

    @AfterEach
    public void tearDown(){
    }

    private SingleMove getNewSingleMove(int file_start, int rank_start, int file_end, int rank_end){
        return new SingleMove(new Position(file_start, rank_start),new Position(file_end, rank_end));
    }


    @Test
    public void testCheck(){
//        board.movePieceTo(getNewSingleMove(4,1,4,8));
        board.movePieceTo(new PromotionMove(new Position(4,8), "queen"));
        assertEquals(PieceType.QUEEN, board.getPieceAt(new Position(4,8)).getType(), "Queen is in 4,8");
        assertEquals(PlayerColor.WHITE, board.getPieceAt(new Position(4,8)).getColor(), "Queen is WHITE");
        assertTrue("King is check", MoveEvaluator
                .isKingCheck(
                        board,
                        BoardScanner.getCurrentPosition(
                                board,
                                PieceType.KING,
                                PlayerColor.BLACK),
                        PlayerColor.WHITE));

    }
}
