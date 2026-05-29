package nl.davefemi.webchess.game;

import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.actions.record.MoveRecord;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.game.rule.RuleEngine;
import java.util.ArrayList;
import java.util.List;
import static nl.davefemi.webchess.game.GameStatus.*;

@Slf4j
public class Game {
    private BoardContext currentBoardContext;
    private boolean gameActive = false;
    private final TurnGenerator turnGenerator;
    private final List<MoveRecord> moveHistory;

    public Game() {
        turnGenerator = new TurnGenerator();
        currentBoardContext = new BoardContext(turnGenerator.peek());
        moveHistory = new ArrayList<>();
    }

    public Game(BoardContext boardContext, boolean gameActive, PieceColor colorToMove,
                List<MoveRecord> moveHistory){
        this.currentBoardContext = boardContext;
        this.gameActive = gameActive;
        this.turnGenerator = new TurnGenerator(colorToMove);
        this.moveHistory = moveHistory;
    }

    public Game(Game other){
        this.currentBoardContext = other.currentBoardContext;
        this.gameActive = other.gameActive;
        this.turnGenerator = new TurnGenerator(other.turnGenerator.peek());
        this.moveHistory = new ArrayList<>(other.moveHistory);

    }

    public void start(){
        gameActive = true;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public PieceColor getColorToMove() throws GameException {
//        if (!gameActive)
//            throw new GameException("Game has ended");
        return turnGenerator.peek();
    }

    public BoardContext getCurrentBoardContext(){
        return new BoardContext(currentBoardContext.getColorToMove(),
                currentBoardContext.getCopyOfBoard(),
                currentBoardContext.getLastMove(),
                currentBoardContext.getCapturedPieces(),
                currentBoardContext.getOriginalRooks());
    }

    public GameStatus getStatus(PieceColor color) throws BoardException {
        if (isPlayerCheckMate(color))
            return CHECKMATE;
        if (isPlayerInCheck(color))
            return CHECK;
        if (isPlayerCheckMate(PieceColor.getOpponent(color)))
            return WINNER;
        if (RuleEngine.getAllLegalMovesByPieceColor(currentBoardContext, color).isEmpty() && !isPlayerInCheck(color))
            return STALEMATE;
        return ACTIVE;
    }


    public List<Move> getAvailableMoves(PieceColor color) throws BoardException {
        return RuleEngine.getAllLegalMovesByPieceColor(getCurrentBoardContext(), color);
    }

    public synchronized boolean executeMove(PieceColor color, Move move) throws GameException, MoveException, BoardException {
        this.currentBoardContext = RuleEngine.applyLegalMove(getCurrentBoardContext(), moveHistory, color, move);
        updateMoveHistory();
        turnGenerator.nextTurn();
        log.info(getLastMove().toString());
        this.currentBoardContext.setColorToMove(turnGenerator.peek());
        if (isPlayerCheckMate(getColorToMove())) {
            gameActive = false;
        }
        return true;
    }

    public void setInactive(){
        gameActive = false;
    }

    public Move getLastMove(){
        if (!moveHistory.isEmpty())
            return moveHistory.getLast().getMove();
        return null;
    }

    public List<MoveRecord> getMoveHistory(){
        return new ArrayList<>(moveHistory);
    }

    private boolean isPlayerInCheck(PieceColor color) throws BoardException {
        return RuleEngine.isKingInCheck(getCurrentBoardContext(), color);
    }

    private boolean isPlayerCheckMate(PieceColor color) throws BoardException {
        return RuleEngine.isPlayerCheckMate(getCurrentBoardContext(), color);
    }

    private synchronized void updateMoveHistory(){
        if (currentBoardContext.getLastMove() != null)
            moveHistory.add(currentBoardContext.getLastMove());
    }


}