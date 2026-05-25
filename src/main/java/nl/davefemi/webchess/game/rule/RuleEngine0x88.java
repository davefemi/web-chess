package nl.davefemi.webchess.game.rule;

import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.board.BoardContext;
import nl.davefemi.webchess.game.board.BoardContext0x88;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.record.MoveRecord;

import java.util.List;

public class RuleEngine0x88 {

    public static BoardContext0x88 applyLegalMove(BoardContext0x88 boardContext, Move move){
        return null;
    }

    public static List<Move> getAllLegalMovesByPieceColor(BoardContext boardContext, PieceColor playerToMove) throws BoardException {
        return RuleEngine.getAllLegalMovesByPieceColor(boardContext, playerToMove);
    }

    public static boolean isKingInCheck(BoardContext boardContext, PieceColor playerToMove) throws BoardException {
        return RuleEngine.isKingInCheck(boardContext, playerToMove);
    }

    public static boolean isPlayerCheckMate(BoardContext boardContext, PieceColor playerToMove) throws BoardException {
        return RuleEngine.isPlayerCheckMate(boardContext, playerToMove);
    }

    public static BoardContext applyLegalMove(BoardContext boardContext, List<MoveRecord> moveHistory, PieceColor pieceColor, Move move) throws BoardException, GameException, MoveException {
        if (!RuleEngine.isMoveAllowed(boardContext, moveHistory, pieceColor, move)){
             if (RuleEngine.isPlayerCheckMate(boardContext, pieceColor))
                    throw new GameException(boardContext.getColorToMove() + " is check mate");
                if (RuleEngine.isKingInCheck(boardContext, pieceColor))
                    throw new MoveException(boardContext.getColorToMove() + " is in check");
                throw new MoveException("Illegal move");
            }
        return boardContext.applyMove(move);
    }

}
