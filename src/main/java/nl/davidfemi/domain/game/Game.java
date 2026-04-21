package nl.davidfemi.domain.game;

import nl.davidfemi.domain.board.Board;
import nl.davidfemi.domain.board.Position;
import nl.davidfemi.domain.game.rules.PromotionVerifier;
import nl.davidfemi.domain.game.rules.RuleEngine;
import nl.davidfemi.domain.game.utility.CastlingMove;
import nl.davidfemi.domain.game.utility.Move;
import nl.davidfemi.domain.pieces.PieceType;
import nl.davidfemi.domain.pieces.PlayerColor;
import nl.davidfemi.exception.MoveException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class Game {
    private Logger logger = Logger.getLogger(Game.class.getName());
    private final UUID gameId;
    private final Board gameBoard;
    private final TurnGenerator turnGenerator;
    private PlayerColor turn;
    private boolean castlingAllowed = true;

    public Game(){
        gameId = UUID.randomUUID();
        gameBoard = new Board();
        turnGenerator = new TurnGenerator();
        turn = turnGenerator.nextTurn();
    }

    public UUID getGameId(){
        return gameId;
    }

    public PlayerColor getTurn(){
        return turn;
    }

    public boolean isCheck(PlayerColor color){
        return RuleEngine.isCheck(this, color);
    }

    public boolean isCheckMate(PlayerColor color){
        return RuleEngine.isCheckMate(this, color);
    }


    public List<Move> getAvailableMoves(PlayerColor color){
        logger.info("Player turn is " + getTurn());
        return RuleEngine.getLegalMoves(this, color);
    }

    public boolean executeMove(PlayerColor color, Move move){
        if (getAvailableMoves(color).isEmpty())
            throw new MoveException(getTurn() + " is check mate");
        if (!RuleEngine.isMoveAllowed(this, color, move))
            throw new MoveException("Illegal move");
        boolean res = gameBoard.movePieceTo(move);
        this.turn = turnGenerator.nextTurn();
        return res;
    }

    public boolean executeCastling(PlayerColor color, CastlingMove move){
        if(getAvailableMoves(color).isEmpty())
            throw new MoveException(getTurn() + " is check mate");
        if (!castlingAllowed || !RuleEngine.isMoveAllowed(this, color, move))
            throw new MoveException("Castling is not allowed");
        boolean res = gameBoard.movePieceTo(move);
        castlingAllowed = false;
        printBoard();
        return res;
    }

    public boolean promotePawn(PlayerColor color, Position position, PieceType pieceType){
        if (PromotionVerifier.isPromotionLegal(getCopyOfBoard(), position, pieceType)){
            gameBoard.promotePawnTo(position, pieceType);
            gameBoard.printBoard();
            return true;
        }
        return false;
    }

    public void printBoard(){
        gameBoard.printBoard();
    }

    public Board getCopyOfBoard() {
        return new Board(gameBoard);
    }
}