package nl.davefemi.chess.session.model;

import lombok.Getter;
import nl.davefemi.chess.play.model.game.Color;
import java.util.UUID;

public final class Player {
    @Getter
    private final UUID id;
    @Getter
    private final String messageEndpointId;
    @Getter
    private final UUID sessionId;
    @Getter
    private final Color color;

    public Player(UUID playerId, String messageEndpointId, UUID sessionId, Color color){
        this.id = playerId;
        this.messageEndpointId = messageEndpointId;
        this.sessionId = sessionId;
        this.color = color;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Player p))
            return false;
        return p.id.equals(id) &&
                p.messageEndpointId.equals(messageEndpointId) &&
                p.sessionId.equals(sessionId) &&
                p.color.equals(color);
    }
}
