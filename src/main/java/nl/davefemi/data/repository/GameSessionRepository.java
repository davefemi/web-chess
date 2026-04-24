package nl.davefemi.data.repository;

import nl.davefemi.data.entity.GameSessionEntity;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;

@Repository
public interface GameSessionRepository {

    GameSessionEntity retrieveGameByGameId(String gameId) throws FileNotFoundException;
    void saveGame(GameSessionEntity game);
}
