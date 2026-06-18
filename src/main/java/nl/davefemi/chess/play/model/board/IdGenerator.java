package nl.davefemi.chess.play.model.board;

public final class IdGenerator {
    private int nextId;

    public IdGenerator(){
        nextId = 100;
    }
    public IdGenerator(int id){
        if (id < 100) {
            throw new IllegalArgumentException("Id must be at least 100");
        }
        nextId = id;
    }

    public int peek(){
        return nextId;
    }

    public int getNextId(){
        return nextId++;
    }
}
