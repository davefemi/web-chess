package nl.davefemi.webchess.data.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.entity.session.GameSessionEntity;
import nl.davefemi.webchess.exception.SessionException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SessionStore implements SessionRepository {
    private final RedisTemplate<String, GameSessionEntity> sessionRedisTemplate;
    private final int sessionTimeToLive = 5;

    @Override
    public GameSessionEntity retrieveGameSessionById(String sessionId) throws SessionException {
        GameSessionEntity sessionEntity = sessionRedisTemplate.opsForValue().get("session: " + sessionId);
        if (sessionEntity == null)
            throw new SessionException("Session not found");
        return sessionEntity;
    }

    @Override
    public void saveGameSession(GameSessionEntity entity) {
        sessionRedisTemplate.opsForValue().set("session: " + entity.getSessionId(), entity, Duration.of(sessionTimeToLive, ChronoUnit.MINUTES));
    }

}
