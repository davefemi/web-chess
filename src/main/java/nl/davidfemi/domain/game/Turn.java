package nl.davidfemi.domain.game;

import nl.davidfemi.domain.pieces.PlayerColor;

public class Turn {
    private PlayerColor nextTurn = PlayerColor.WHITE;

    public PlayerColor nextTurn(){
        PlayerColor turn = nextTurn;
        if (nextTurn == PlayerColor.BLACK) {
            nextTurn = PlayerColor.WHITE;
        }
        else {
            nextTurn = PlayerColor.BLACK;
        }
        return turn;
    }

    public boolean hasTurn(PlayerColor playerColor){
        return nextTurn == playerColor;
    }
}
