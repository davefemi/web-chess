package nl.davidfemi.domain.game;

import nl.davidfemi.domain.board.Board;

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
}
