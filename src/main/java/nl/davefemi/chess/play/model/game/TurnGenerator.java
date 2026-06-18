package nl.davefemi.chess.play.model.game;

final class TurnGenerator{
    private Color nextTurn;

    TurnGenerator(){
        nextTurn = Color.WHITE;
    }
    TurnGenerator(Color firstTurn){
        this.nextTurn = firstTurn;
    }

    protected Color nextTurn(){
        Color turn = nextTurn;
        if (nextTurn == Color.BLACK) {
            nextTurn = Color.WHITE;
        }
        else {
            nextTurn = Color.BLACK;
        }
        return turn;
    }

    protected Color peek(){
        return nextTurn;
    }
}
