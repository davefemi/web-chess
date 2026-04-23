package nl.davefemi.domain.game;

public class PieceIdGenerator {
    private int nextId;

    public PieceIdGenerator(){
        nextId = 100;
    }
    protected PieceIdGenerator(int id){
        nextId = id;
    }

    protected int peek(){
        return nextId;
    }

    public int getNextId(){
        return nextId++;
    }
}
