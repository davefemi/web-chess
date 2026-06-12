package nl.davefemi.webchess.game;

import lombok.Getter;
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
    @Getter
    private int currentRound;
    private GameBoardContext currentGameBoardContext;
    @Getter
    private GameStatus status = GameStatus.waiting();
    private final TurnGenerator turnGenerator;
    @Getter
    private boolean inCheck;
    private final List<MoveRecord> moveHistory;

    public Game() {
        currentRound = 1;
        turnGenerator = new TurnGenerator();
        currentGameBoardContext = new GameBoardContext(turnGenerator.peek());
        moveHistory = new ArrayList<>();
    }

    public Game(int currentRound, GameBoardContext boardContext, GameStatus status, Color sideToMove, boolean inCheck,
                List<MoveRecord> moveHistory){
        this.currentRound = currentRound;
        this.currentGameBoardContext = boardContext;
        this.status = status;
        this.inCheck = inCheck;
        this.turnGenerator = new TurnGenerator(sideToMove);
        currentGameBoardContext.setSideToMove(turnGenerator.peek());
        this.moveHistory = moveHistory;
    }

    public Game(Game other){
        this.currentRound = other.currentRound;
        this.currentGameBoardContext = other.currentGameBoardContext;
        this.status = other.status;
        this.inCheck = other.inCheck;
        this.turnGenerator = new TurnGenerator(other.turnGenerator.peek());
        this.moveHistory = new ArrayList<>(other.moveHistory);
    }

    public void start() throws GameException {
        if (status.isFinished())
            throw new GameException("Can't restart game that has ended");
        status = GameStatus.active();
    }

    public Color getSideToMove() throws GameException {
        if (!status.isFinished())
            return turnGenerator.peek();
        return null;
    }

    public GameBoardContext getGameBoardContext(){
        return new GameBoardContext(currentGameBoardContext.getSideToMove(),
                currentGameBoardContext.getCopyOfBoard(),
                currentGameBoardContext.getLastMove(),
                currentGameBoardContext.getCapturedPieces(),
                currentGameBoardContext.getOriginalRooks());
    }

    public boolean isCheck(Color color) throws BoardException {
        return RuleEngine.isCheck(getGameBoardContext(), color);
    }


    public List<Move> getAvailableMoves(Color color) throws BoardException, GameException {
        if (status.isWaiting())
            throw new GameException("Game is not active yet");
        if (status.isFinished())
            throw new GameException("Game has ended");
        return RuleEngine.getAllLegalMovesByColor(getGameBoardContext(), color);
    }

    public synchronized boolean executeMove(Color color, Move move) throws GameException, MoveException, BoardException {
        if (status.isWaiting())
            throw new GameException("Game is not active yet");
        if (status.isFinished())
            throw new GameException("Game has ended");
        if (!(color == getSideToMove()))
            throw new MoveException("It is not " + color + "'s turn yet");
        this.currentGameBoardContext = RuleEngine.applyMove(getGameBoardContext(), moveHistory, color, move);
        updateMoveHistory();
        turnGenerator.nextTurn();
        this.currentGameBoardContext.setSideToMove(turnGenerator.peek());
        inCheck = RuleEngine.isCheck(currentGameBoardContext, getSideToMove());
        status = RuleEngine.evaluateStatus(currentGameBoardContext);
        if (status.isActive())
            currentRound++;
        return true;
    }

    public void surrender(Color color){
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