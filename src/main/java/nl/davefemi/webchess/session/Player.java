package nl.davefemi.webchess.session;

import lombok.Getter;
import nl.davefemi.webchess.game.Color;
import java.util.UUID;

public final class Player {
    @Getter
    private final UUID id;
    @Getter
    private final String messageId;
    @Getter
    private final UUID sessionId;
    @Getter
    private final Color color;

    public Player(UUID playerId, String messageId, UUID sessionId, Color color){
        this.id = playerId;
        this.messageId = messageId;
        this.sessionId = sessionId;
        this.color = color;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Player p))
            return false;
        return p.id.equals(id) &&
                p.messageId.equals(messageId) &&
                p.sessionId.equals(sessionId) &&
                p.color.equals(color);
    }
}
