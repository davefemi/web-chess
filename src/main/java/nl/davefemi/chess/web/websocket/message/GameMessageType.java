package nl.davefemi.chess.web.websocket.message;

public enum GameMessageType {
    SESSION_UPDATE("session_update"),
    GAME_STATE_UPDATED("game_state_updated"),
    COMMAND_RESPONSE("command_response"),
    ERROR("error");

    private final String type;

    GameMessageType(String type){
        this.type = type;
    }
    @Override
    public String toString(){
        return type;
    }
}
