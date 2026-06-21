package nl.davefemi.chess.web.websocket.event;

public enum EventType {
    PLAYER_SUBSCRIBED("player_subscribed"),
    MOVE_REQUESTED("move_requested"),
    MOVE_ACCEPTED("move_accepted"),
    MOVE_REJECTED("move_rejected"),
    MOVE_COMPLETED("move_completed"),
    REMATCH_REQUESTED("rematch_requested"),
    REMATCH_ACCEPTED("rematch_accepted"),
    REMATCH_DECLINED("rematch_declined"),
    PLAYER_SURRENDERED("player_surrendered"),
    GAME_ENDED("game_ended"),
    SESSION_ENDED("session_ended");

    private final String type;

    EventType(String type){
        this.type = type;
    }

    @Override
    public String toString(){
        return type;
    }
}
