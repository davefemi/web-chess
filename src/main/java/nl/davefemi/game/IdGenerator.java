package nl.davefemi.game;

public class IdGenerator {
    private int nextId;

    public IdGenerator(){
        nextId = 100;
    }
    protected IdGenerator(int id){
        nextId = id;
    }

    protected int peek(){
        return nextId;
    }

    public int getNextId(){
        return nextId++;
    }
}
