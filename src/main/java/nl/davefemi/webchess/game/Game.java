package nl.davefemi.webchess.game;

import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.actions.move.Move;
import nl.davefemi.webchess.game.actions.MoveRecord;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.rule.RuleEngine;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Game {
    private GameBoardContext currentGameBoardContext;
    private GameStatus status = GameStatus.waiting();
    private final TurnGenerator turnGenerator;
    private final List<MoveRecord> moveHistory;

    public Game() {
        turnGenerator = new TurnGenerator();
        currentGameBoardContext = new GameBoardContext(turnGenerator.peek());
        moveHistory = new ArrayList<>();
    }

    public Game(GameBoardContext boardContext, GameStatus status, PieceColor colorToMove,
                List<MoveRecord> moveHistory){
        this.currentGameBoardContext = boardContext;
        this.status = status;
        this.turnGenerator = new TurnGenerator(colorToMove);
        currentGameBoardContext.setColorToMove(turnGenerator.peek());
        this.moveHistory = moveHistory;
    }

    public Game(Game other){
        this.currentGameBoardContext = other.currentGameBoardContext;
        this.status = other.status;
        this.turnGenerator = new TurnGenerator(other.turnGenerator.peek());
        this.moveHistory = new ArrayList<>(other.moveHistory);

    }

    public void start() throws GameException {
        if (status.isFinished())
            throw new GameException("Can't restart game that has ended");
        status = GameStatus.active();
    }

    public GameStatus getStatus() {
        return status;
    }

    public PieceColor getColorToMove() throws GameException {
        if (!status.isFinished())
            return turnGenerator.peek();
        return null;
    }

    public GameBoardContext getGameBoardContext(){
        return new GameBoardContext(currentGameBoardContext.getColorToMove(),
                currentGameBoardContext.getCopyOfBoard(),
                currentGameBoardContext.getLastMove(),
                currentGameBoardContext.getCapturedPieces(),
                currentGameBoardContext.getOriginalRooks());
    }

    public boolean isPlayerInCheck(PieceColor color) throws BoardException {
        return RuleEngine.isKingInCheck(getGameBoardContext(), color);
    }


    public List<Move> getAvailableMoves(PieceColor color) throws BoardException, GameException {
        if (status.isWaiting())
            throw new GameException("Game is not active yet");
        if (status.isFinished())
            throw new GameException("Game has ended");
        return RuleEngine.getAllLegalMovesByPieceColor(getGameBoardContext(), color);
    }

    public synchronized boolean executeMove(PieceColor color, Move move) throws GameException, MoveException, BoardException {
        if (status.isWaiting())
            throw new GameException("Game is not active yet");
        if (status.isFinished())
            throw new GameException("Game has ended");
        this.currentGameBoardContext = RuleEngine.applyMove(getGameBoardContext(), moveHistory, color, move);
        updateMoveHistory();
        turnGenerator.nextTurn();
        this.currentGameBoardContext.setColorToMove(turnGenerator.peek());
        status = RuleEngine.evaluateStatus(currentGameBoardContext);
        return true;
    }

    public void surrender(PieceColor color){
        status = GameStatus.surrender(color);
    }

    public Move getLastMove(){
        if (!moveHistory.isEmpty())
            return moveHistory.getLast().getMove();
        return null;
    }

    public List<MoveRecord> getMoveHistory(){
        return new ArrayList<>(moveHistory);
    }

    private synchronized void updateMoveHistory(){
        if (currentGameBoardContext.getLastMove() != null)
            moveHistory.add(currentGameBoardContext.getLastMove());
    }
}