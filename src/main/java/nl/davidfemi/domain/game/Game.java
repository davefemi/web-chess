package nl.davidfemi.domain.game;

import nl.davidfemi.domain.board.Board;
import nl.davidfemi.domain.pieces.PlayerColor;
import nl.davidfemi.exception.MoveException;
import java.util.List;
import java.util.UUID;

public class Game {
    private final UUID gameId;
    private final Board gameBoard;
    private final Turn playerTurn;

    public Game(){
        gameId = UUID.randomUUID();
        gameBoard = new Board();
        playerTurn = new Turn();
    }

    public UUID getGameId(){
        return gameId;
    }

    public PlayerColor getTurn(){
        return playerTurn.nextTurn();
    }

    public List<Move> nextMove(){
        playerTurn.nextTurn();
        return MoveGenerator.getLegalMoves(this);
    }

    public boolean setMove(Move move){
        if (!MoveGenerator.isMoveAllowed(this, move))
            throw new MoveException("Illegal move");
        return gameBoard.movePieceTo(move);
    }

    public void printBoard(){
        gameBoard.printBoard();
    }

    public Board getGameBoard() {
        return gameBoard;
    }
}