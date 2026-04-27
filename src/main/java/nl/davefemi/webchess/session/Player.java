package nl.davefemi.webchess.session;

import nl.davefemi.webchess.game.board.PieceColor;

import java.util.UUID;

public class Player {
    private final UUID id;
    private final PieceColor playerColor;

    public Player(UUID playerId, PieceColor playerColor){
        this.id = playerId;
        this.playerColor = playerColor;
    }

    public UUID getId(){
        return id;
    }

    public PieceColor getPlayerColor(){
        return playerColor;
    }
}
