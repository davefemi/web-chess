package nl.davidfemi.domain.game;

import nl.davidfemi.domain.pieces.PlayerColor;

public class TurnGenerator {
    private static PlayerColor nextTurn = PlayerColor.WHITE;

    protected PlayerColor nextTurn(){
        PlayerColor turn = nextTurn;
        if (nextTurn == PlayerColor.BLACK) {
            nextTurn = PlayerColor.WHITE;
        }
        else {
            nextTurn = PlayerColor.BLACK;
        }
        return turn;
    }
}
