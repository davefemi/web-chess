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
    private BoardContext currentBoardContext;
    private GameStatus status = GameStatus.waiting();
    private final TurnGenerator turnGenerator;
    private final List<MoveRecord> moveHistory;

    public Game() {
        turnGenerator = new TurnGenerator();
        currentBoardContext = new BoardContext(turnGenerator.peek());
        moveHistory = new ArrayList<>();
    }

    public Game(BoardContext boardContext, GameStatus status, PieceColor colorToMove,
                List<MoveRecord> moveHistory){
        this.currentBoardContext = boardContext;
        this.status = status;
        this.turnGenerator = new TurnGenerator(colorToMove);
        this.moveHistory = moveHistory;
    }

    public Game(Game other){
        this.currentBoardContext = other.currentBoardContext;
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

    public BoardContext getCurrentBoardContext(){
        return new BoardContext(currentBoardContext.getColorToMove(),
                currentBoardContext.getCopyOfBoard(),
                currentBoardContext.getLastMove(),
                currentBoardContext.getCapturedPieces(),
                currentBoardContext.getOriginalRooks());
    }

    public boolean isPlayerInCheck(PieceColor color) throws BoardException {
        return RuleEngine.isKingInCheck(getCurrentBoardContext(), color);
    }


    public List<Move> getAvailableMoves(PieceColor color) throws BoardException, GameException {
        if (!getStatus().isActive())
            throw new GameException("Game is not active");
        return RuleEngine.getAllLegalMovesByPieceColor(getCurrentBoardContext(), color);
    }

    public synchronized boolean executeMove(PieceColor color, Move move) throws GameException, MoveException, BoardException {
        if (!status.isActive())
            throw new GameException("Game is not active");
        this.currentBoardContext = RuleEngine.applyLegalMove(getCurrentBoardContext(), moveHistory, color, move);
        updateMoveHistory();
        turnGenerator.nextTurn();
        log.info(getLastMove().toString());
        this.currentBoardContext.setColorToMove(turnGenerator.peek());
        status = RuleEngine.evaluateStatus(currentBoardContext);
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
        if (currentBoardContext.getLastMove() != null)
            moveHistory.add(currentBoardContext.getLastMove());
    }
}