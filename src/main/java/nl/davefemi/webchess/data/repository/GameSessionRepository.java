package nl.davefemi.webchess.data.repository;

import nl.davefemi.webchess.data.entity.AccessCodeEntity;
import nl.davefemi.webchess.data.entity.GameSessionEntity;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;

@Repository
public interface GameSessionRepository {

    GameSessionEntity retrieveGameSessionById(String gameId) throws FileNotFoundException;
    void saveGameSession(GameSessionEntity game);
    void saveAccessCode(AccessCodeEntity accessCode, int timeToLive);
    AccessCodeEntity retrieveAccessCode(String accessCode) throws FileNotFoundException;

}
