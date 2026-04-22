package nl.davefemi.domain.game;

import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.game.move.*;
import nl.davefemi.domain.game.rule.RuleEngine;
import nl.davefemi.domain.piece.Piece;
import nl.davefemi.domain.piece.PieceType;
import nl.davefemi.domain.piece.PlayerColor;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.MoveException;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class Game {
    private final UUID gameId;
    private final Board gameBoard;
    private final TurnGenerator turnGenerator;
    private PlayerColor turn;
    private final List<MoveRecord> moveHistory;
    private final List<Piece> capturedPieces = new ArrayList<>();

    public Game(){
        gameId = UUID.randomUUID();
        gameBoard = new Board();
        turnGenerator = new TurnGenerator();
        turn = turnGenerator.nextTurn();
        moveHistory = new ArrayList<>();
    }

    public UUID getGameId(){
        return gameId;
    }

    public PlayerColor getTurn(){
        return turn;
    }

    public Board getCopyOfBoard() {
        return new Board(gameBoard);
    }

    public boolean isCheck(PlayerColor color){
        return RuleEngine.isCheck(this, color);
    }

    public boolean isCheckMate(PlayerColor color){
        return RuleEngine.isCheckMate(this, color);
    }

    public List<Move> getAvailableMoves(PlayerColor color){
        return RuleEngine.getLegalMoves(this, color);
    }

    public boolean executeMove(PlayerColor color, Move move){
        if (!RuleEngine.isMoveAllowed(this, color, move)) {
            if (isCheck(color))
                throw new MoveException(color + " is in check");
            if (isCheckMate(color))
                throw new GameException(color + " is check mate");
            throw new MoveException("Illegal move");
        }
        Piece p = gameBoard.movePieceTo(move);
        PieceType pieceType = null;
        if (move instanceof SingleMove s) {
            pieceType = gameBoard.getPieceAt(s.to()).getType();
        }
        if (!RuleEngine.checkForPromotion(this))
            this.turn = turnGenerator.nextTurn();
        updateMoveHistory(move, color, pieceType, p);
        return true;
    }

    private void updateMoveHistory(Move move, PlayerColor color, PieceType pieceType, Piece capturedPiece){
        boolean pieceCaptured = capturedPiece != null;
        if (pieceCaptured)
            capturedPieces.add(capturedPiece);
        moveHistory.add(new MoveRecord(move, color, pieceType, pieceCaptured
                ? capturedPiece.getType()
                : null, false));
    }

    public MoveRecord getLastMove(){
        return MoveRecordBuilder.duplicateMoveRecord(moveHistory.getLast());
    }
}