package nl.davefemi.webchess.data.repository;

import nl.davefemi.webchess.data.entity.session.GameSessionEntity;
import nl.davefemi.webchess.exception.SessionException;
import java.io.FileNotFoundException;

public interface SessionRepository {

    GameSessionEntity retrieveGameSessionById(String gameId) throws FileNotFoundException, SessionException;
    void saveGameSession(GameSessionEntity game);
}
