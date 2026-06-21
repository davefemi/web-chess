package nl.davefemi.chess.web.websocket.event;

import nl.davefemi.chess.session.model.Player;

import java.util.UUID;

public record GameEvent<EventType>(EventType type, UUID sessionId, Player actionBy) {
}
