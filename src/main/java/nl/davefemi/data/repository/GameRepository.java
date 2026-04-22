package nl.davefemi.data.repository;

import nl.davefemi.domain.game.Game;
import org.springframework.stereotype.Component;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameRepository {
    public final ConcurrentHashMap<UUID, Game> games = new ConcurrentHashMap<>();

    public Game saveGame(Game game){
        games.put(game.getGameId(), game);
        return game;
    }

    public Game find(UUID gameId){
        return games.get(gameId);
    }


}
