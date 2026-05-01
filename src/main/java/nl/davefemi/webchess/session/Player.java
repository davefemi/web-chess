package nl.davefemi.webchess.session;

import nl.davefemi.webchess.game.board.PieceColor;

import java.util.UUID;

public class Player {
    private final UUID id;
    private final PieceColor playingColor;

    public Player(UUID playerId, PieceColor playingColor){
        this.id = playerId;
        this.playingColor = playingColor;
    }

    public UUID getId(){
        return id;
    }

    public PieceColor getPlayingColor(){
        return playingColor;
    }
}
