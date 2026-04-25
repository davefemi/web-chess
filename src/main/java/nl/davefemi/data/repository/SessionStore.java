package nl.davefemi.data.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.data.entity.GameSessionEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SessionStore implements GameSessionRepository {
    private final RedisTemplate<String, GameSessionEntity> redisTemplate;
    private final int timeToLive = 60;

    @Override
    public GameSessionEntity retrieveGameSessionById(String sessionId) throws FileNotFoundException {
        GameSessionEntity sessionEntity = redisTemplate.opsForValue().get("session: " + sessionId);
        if (sessionEntity == null)
            throw new FileNotFoundException("Session not found");
        return sessionEntity;
    }

    @Override
    public void saveGameSession(GameSessionEntity game) {
        redisTemplate.opsForValue().set("session: " + game.getSessionId(), game, Duration.of(timeToLive, ChronoUnit.HOURS));
    }
}
