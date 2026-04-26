package nl.davefemi.data.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.data.entity.AccessCodeEntity;
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
    private final RedisTemplate<String, AccessCodeEntity> accessCodeRedisTemplate;
    private final int sessionTimeToLive = 2;

    @Override
    public GameSessionEntity retrieveGameSessionById(String sessionId) throws FileNotFoundException {
        GameSessionEntity sessionEntity = redisTemplate.opsForValue().get("session: " + sessionId);
        if (sessionEntity == null)
            throw new FileNotFoundException("Session not found");
        return sessionEntity;
    }

    @Override
    public void saveGameSession(GameSessionEntity entity) {
        redisTemplate.opsForValue().set("session: " + entity.getSessionId(), entity, Duration.of(sessionTimeToLive, ChronoUnit.HOURS));
    }

    @Override
    public void saveAccessCode(AccessCodeEntity accessCode, int timeToLive){
        accessCodeRedisTemplate.opsForValue().set("access_code: " + accessCode.getToken(), accessCode, Duration.of(timeToLive, ChronoUnit.SECONDS));
    }

    @Override
    public AccessCodeEntity retrieveAccessCode(String code) throws FileNotFoundException {
        AccessCodeEntity accessCode = accessCodeRedisTemplate.opsForValue().get("access_code: " + code);
        if (accessCode == null)
            throw new FileNotFoundException("Code not found");
        return accessCode;
    }
}
