package nl.davefemi.webchess.session;

import lombok.Getter;
import nl.davefemi.webchess.game.Color;

import java.util.UUID;

public final class Player {
    @Getter
    private final UUID id;
    @Getter
    private final Color color;

    public Player(UUID playerId, Color color){
        this.id = playerId;
        this.color = color;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Player))
            return false;
        Player p = (Player) o;
        return p.id.equals(id) && p.color.equals(color);
    }
}
