package nl.davefemi.webchess.game;

public class IdGenerator {
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
