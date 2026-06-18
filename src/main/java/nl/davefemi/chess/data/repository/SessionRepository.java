package nl.davefemi.chess.data.repository;

import nl.davefemi.chess.data.entity.session.GameSessionEntity;
import nl.davefemi.chess.exception.SessionNotFoundException;

public interface SessionRepository {

    GameSessionEntity retrieveGameSessionById(String gameId) throws SessionNotFoundException;
    void saveGameSession(GameSessionEntity game);
}
