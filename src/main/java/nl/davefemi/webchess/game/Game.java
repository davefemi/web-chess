package nl.davefemi.webchess.game;

import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.game.board.*;
import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.record.MoveRecord;
import nl.davefemi.webchess.game.record.MoveRecordBuilder;
import nl.davefemi.webchess.game.rule.RuleEngine;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import java.util.ArrayList;
import java.util.List;
import static nl.davefemi.webchess.game.GameStatus.*;

@Slf4j
public class Game {
    private final IdGenerator pieceIdGenerator;
    private boolean gameActive = false;
    private final Board gameBoard;
    private final TurnGenerator turnGenerator;
    private final List<MoveRecord> moveHistory;
    private final List<Piece> capturedPieces;
    private final List<Integer> originalRooks;

    public Game() {
        this.pieceIdGenerator = new IdGenerator();
        gameBoard = new Board(pieceIdGenerator);
        turnGenerator = new TurnGenerator();
        moveHistory = new ArrayList<>();
        this.capturedPieces = new ArrayList<>();
        this.originalRooks = new ArrayList<>(fetchOriginalRooksFromBoard());
    }

    public Game(int nextPieceId, Board board, boolean gameActive, PieceColor turn,
                List<MoveRecord> moveHistory, List<Piece> capturedPieces, List<Integer> originalRooks){
        this.pieceIdGenerator = new IdGenerator(nextPieceId);
        this.gameBoard = board;
        this.gameActive = gameActive;
        this.turnGenerator = new TurnGenerator(turn);
        this.moveHistory = moveHistory;
        this.capturedPieces = new ArrayList<>(capturedPieces);
        this.originalRooks = new ArrayList<>(originalRooks);
    }

    public Game(Game other){
        this.pieceIdGenerator = new IdGenerator(other.pieceIdGenerator.peek());
        this.gameActive = other.gameActive;
        this.gameBoard = other.getCopyOfBoard();
        this.turnGenerator = new TurnGenerator(other.turnGenerator.peek());
        this.moveHistory = new ArrayList<>(other.moveHistory);
        this.capturedPieces = new ArrayList<>(other.capturedPieces);
        this.originalRooks = new ArrayList<>(other.originalRooks);
    }

    public void start(){
        gameActive = true;
    }

    public int getLatestPieceId(){
        return pieceIdGenerator.peek();
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public PieceColor getPlayerTurn() throws GameException {
//        if (!gameActive)
//            throw new GameException("Game has ended");
        return turnGenerator.peek();
    }

    public GameStatus getStatus(PieceColor color) throws BoardException {
        if (isPlayerCheckMate(color))
            return CHECKMATE;
        if (isPlayerInCheck(color))
            return CHECK;
        if (isPlayerCheckMate(PieceColor.getOpponent(color)))
            return WINNER;
        if (RuleEngine.getAllLegalMovesByPieceColor(this, color).isEmpty() && !isPlayerInCheck(color))
            return STALEMATE;
        return ACTIVE;
    }

    public Board getCopyOfBoard() {
        return new Board(gameBoard);
    }

    public List<Move> getAvailableMoves(PieceColor color) throws BoardException {
        return RuleEngine.getAllLegalMovesByPieceColor(this, color);
    }

    public boolean executeMove(PieceColor color, Move move) throws GameException, MoveException, BoardException {
        if (!RuleEngine.isMoveAllowed(this, color, move)) {
            if (isPlayerCheckMate(color))
                throw new GameException(color + " is check mate");
            if (isPlayerInCheck(color))
                throw new MoveException(color + " is in check");
            throw new MoveException("Illegal move");
        }
        synchronized (this) {
            Piece p = gameBoard.applyValidatedMove(pieceIdGenerator, move);
            turnGenerator.nextTurn();
            if (isPlayerCheckMate(getPlayerTurn())) {
                gameActive = false;
            }
                updateMoveHistory(move, color, p);
                log.info(getLastMove().toString());
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

    public List<Piece> getCapturedPieces(){
        return new ArrayList<>(capturedPieces);
    }

    public List<Integer> getOriginalRooks(){
        return new ArrayList<>(originalRooks);
    }

    private List<Integer> fetchOriginalRooksFromBoard() {
        List<Integer> originalRooks = new ArrayList<>();
        for (Position p: gameBoard.getBoardPositions()){
            Piece piece = gameBoard.getPieceAt(p);
            if (piece != null && piece.getType() == PieceType.ROOK)
                originalRooks.add(piece.getId());
        }
        return originalRooks;
    }

    private boolean isPlayerInCheck(PieceColor color) throws BoardException {
        return RuleEngine.isKingInCheck(this, color);
    }

    private boolean isPlayerCheckMate(PieceColor color) throws BoardException {
        return RuleEngine.isPlayerCheckMate(this, color);
    }

    private synchronized void updateMoveHistory(Move move, PieceColor color, Piece capturedPiece){
        if (capturedPiece != null)
            capturedPieces.add(capturedPiece);
        moveHistory.add(MoveRecordBuilder.getMoveRecord(getCopyOfBoard(), move, color, capturedPiece));
    }
}