package nl.davefemi.webchess.game;

public enum GameStatus {
    ACTIVE("active"),
    CHECK("check"),
    CHECKMATE("check-mate"),
    WINNER("winner"),
    STALEMATE("stalemate");


    private final String status;
    GameStatus(String status){
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public static GameStatus fromString(String type){
        for (GameStatus s : values()){
            if (s.status.equalsIgnoreCase(type))
                return s;
        }
        throw new IllegalArgumentException("Status does not exist");
    }
}
