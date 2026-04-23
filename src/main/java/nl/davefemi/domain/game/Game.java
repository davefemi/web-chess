package nl.davefemi.domain.game;

import lombok.extern.slf4j.Slf4j;
import nl.davefemi.domain.board.Board;
import nl.davefemi.domain.game.actions.move.Move;
import nl.davefemi.domain.game.actions.record.MoveRecord;
import nl.davefemi.domain.game.actions.record.MoveRecordBuilder;
import nl.davefemi.domain.game.rule.RuleEngine;
import nl.davefemi.domain.board.Piece;
import nl.davefemi.domain.board.PlayerColor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.MoveException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class Game {
    private final PieceIdGenerator pieceIdGenerator;
    private final UUID gameId;
    private boolean gameActive = true;
    private final Board gameBoard;
    private TurnGenerator turnGenerator;
    private final List<MoveRecord> moveHistory;
    private final List<Piece> capturedPieces;

    public Game(){
        this.pieceIdGenerator = new PieceIdGenerator();
        gameId = UUID.randomUUID();
        gameBoard = new Board(pieceIdGenerator);
        turnGenerator = new TurnGenerator();
        moveHistory = new ArrayList<>();
        this.capturedPieces = new ArrayList<>();
    }

    public Game(UUID gameId, int nextPieceId, Board board, boolean gameActive, PlayerColor turn,
                List<MoveRecord> moveHistory, List<Piece> capturedPieces){
        this.pieceIdGenerator = new PieceIdGenerator(nextPieceId);
        this.gameId = gameId;
        this.gameBoard = board;
        this.gameActive = gameActive;
        this.turnGenerator = new TurnGenerator(turn);
        this.moveHistory = moveHistory;
        this.capturedPieces = capturedPieces;
    }

    public UUID getGameId(){
        return gameId;
    }

    public int getLatestPieceId(){
        return pieceIdGenerator.peek();
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public PlayerColor getPlayerTurn() throws GameException {
        if (turnGenerator == null)
            throw new GameException("Game has ended");
        return turnGenerator.peek();
    }

    public Board getCopyOfBoard() {
        return new Board(gameBoard);
    }

    public boolean isCheck(PlayerColor color) throws BoardException {
        return RuleEngine.isCheck(this, color);
    }

    public boolean isCheckMate(PlayerColor color) throws BoardException {
        return RuleEngine.isCheckMate(this, color);
    }

    public List<Move> getAvailableMoves(PlayerColor color) throws BoardException {
        return RuleEngine.getLegalMoves(this, color);
    }

    public boolean executeMove(PlayerColor color, Move move) throws GameException, MoveException, BoardException {
        if (!RuleEngine.isMoveAllowed(this, color, move)) {
            if (isCheckMate(color))
                throw new GameException(color + " is check mate");
            if (isCheck(color))
                throw new MoveException(color + " is in check");
            throw new MoveException("Illegal move");
        }
        Piece p = gameBoard.movePieceTo(pieceIdGenerator, move);
        if (!RuleEngine.checkForPromotion(this))
            turnGenerator.nextTurn();
        if (isCheckMate(getPlayerTurn())) {
            gameActive = false;
            turnGenerator = null;
        }
        updateMoveHistory(move, color, p);
        log.info(getLastMove().toString());
        return true;
    }

    private void updateMoveHistory(Move move, PlayerColor color, Piece capturedPiece){
        if (capturedPiece != null)
            capturedPieces.add(capturedPiece);
        moveHistory.add(MoveRecordBuilder.getMoveRecord(getCopyOfBoard(), move, color, capturedPiece));
    }

    public MoveRecord getLastMove(){
        return moveHistory.getLast();
    }

    public List<MoveRecord> getMoveHistory(){
        return new ArrayList<>(moveHistory);
    }

    public List<Piece> getCapturedPieces(){
        return new ArrayList<>(capturedPieces);
    }
}