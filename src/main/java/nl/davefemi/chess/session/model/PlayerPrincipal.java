package nl.davefemi.chess.session.model;

import java.security.Principal;

public record PlayerPrincipal(Player player) implements Principal {

    @Override
    public String getName() {
        return player.getColor().toString();
    }
}
