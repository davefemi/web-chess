package nl.davefemi.chess.session.model;

import lombok.Getter;
import nl.davefemi.chess.gameplay.model.game.Color;
import java.util.UUID;

public final class Player {
    @Getter
    private final UUID id;
    @Getter
    private final String channelId;
    @Getter
    private final UUID sessionId;
    @Getter
    private final Color color;

    public Player(UUID playerId, String channelId, UUID sessionId, Color color){
        this.id = playerId;
        this.channelId = channelId;
        this.sessionId = sessionId;
        this.color = color;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Player p)) {
            return false;
        }
        return p.id.equals(id) &&
                p.channelId.equals(channelId) &&
                p.sessionId.equals(sessionId) &&
                p.color.equals(color);
    }
}
