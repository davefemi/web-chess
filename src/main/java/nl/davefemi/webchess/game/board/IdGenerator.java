package nl.davefemi.webchess.game.board;

public final class IdGenerator {
    private int nextId;

    public IdGenerator(){
        nextId = 100;
    }
    public IdGenerator(int id){
        nextId = id;
    }

    public int peek(){
        return nextId;
    }

    public int getNextId(){
        return nextId++;
    }
}
