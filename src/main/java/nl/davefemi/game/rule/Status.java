package nl.davefemi.game.rule;

public enum Status {
    ACTIVE("active"),
    CHECK("check"),
    CHECKMATE("check-mate"),
    WINNER("winner"),
    STALEMATE("stalemate");


    private final String status;
    Status(String status){
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public static Status fromString(String type){
        for (Status s : values()){
            if (s.status.equalsIgnoreCase(type))
                return s;
        }
        throw new IllegalArgumentException("Status does not exist");
    }
}
