package nl.davefemi.game;

import nl.davefemi.game.board.PieceColor;

public class TurnGenerator{
    private PieceColor nextTurn;

    protected TurnGenerator(){
        nextTurn = PieceColor.WHITE;
    }
    protected TurnGenerator(PieceColor firstTurn){
        this.nextTurn = firstTurn;
    }

    protected PieceColor nextTurn(){
        PieceColor turn = nextTurn;
        if (nextTurn == PieceColor.BLACK) {
            nextTurn = PieceColor.WHITE;
        }
        else {
            nextTurn = PieceColor.BLACK;
        }
        return turn;
    }

    protected PieceColor peek(){
        return nextTurn;
    }
}
