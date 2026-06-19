package nl.davefemi.chess.http.websocket.event;

public enum EventType {
    PLAYER_SUBSCRIBED,
    MOVE_ACCEPTED,
    MOVE_REJECTED,
    MOVE_COMPLETED,
    REMATCH_REQUESTED,
    REMATCH_ACCEPTED,
    REMATCH_DECLINED,
    PLAYER_SURRENDERED,
    GAME_ENDED,
}
