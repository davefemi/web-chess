package nl.davefemi.domain.game;

import nl.davefemi.domain.board.PlayerColor;

public class TurnGenerator{
    private PlayerColor nextTurn;

    protected TurnGenerator(){
        nextTurn = PlayerColor.WHITE;
    }
    protected TurnGenerator(PlayerColor firstTurn){
        this.nextTurn = firstTurn;
    }

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

    protected PlayerColor peek(){
        return nextTurn;
    }
}
