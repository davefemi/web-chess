package nl.davefemi.chess.data.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.data.entity.session.GameSessionEntity;
import nl.davefemi.chess.exception.SessionNotFoundException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SessionStore implements SessionRepository {
    private final RedisTemplate<String, GameSessionEntity> redisTemplate;
    private final int sessionTimeToLive = 5;

    @Override
    public GameSessionEntity retrieveGameSessionById(String sessionId) throws SessionNotFoundException {
        GameSessionEntity sessionEntity = redisTemplate.opsForValue().get("session: " + sessionId);
        if (sessionEntity == null)
            throw new SessionNotFoundException("Session not found");
        return sessionEntity;
    }

    @Override
    public void saveGameSession(GameSessionEntity entity) {
        redisTemplate.opsForValue().set("session: " + entity.getSessionId(), entity, Duration.of(sessionTimeToLive, ChronoUnit.MINUTES));
    }
}
