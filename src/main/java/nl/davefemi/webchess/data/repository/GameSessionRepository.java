package nl.davefemi.webchess.data.repository;

import nl.davefemi.webchess.data.entity.AccessCodeEntity;
import nl.davefemi.webchess.data.entity.GameSessionEntity;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.exception.UnauthorizedException;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;

@Repository
public interface GameSessionRepository {

    GameSessionEntity retrieveGameSessionById(String gameId) throws FileNotFoundException, SessionException;
    void saveGameSession(GameSessionEntity game);
    void saveAccessCode(AccessCodeEntity accessCode, int timeToLive);
    AccessCodeEntity retrieveAccessCode(String accessCode) throws UnauthorizedException;

}
