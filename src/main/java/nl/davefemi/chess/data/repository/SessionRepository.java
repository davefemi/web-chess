package nl.davefemi.chess.data.repository;

import nl.davefemi.chess.data.entity.session.GameSessionEntity;
import nl.davefemi.chess.exception.SessionException;
import java.io.FileNotFoundException;

public interface SessionRepository {

    GameSessionEntity retrieveGameSessionById(String gameId) throws FileNotFoundException, SessionException;
    void saveGameSession(GameSessionEntity game);
}
