package nl.davefemi.webchess.data.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.entity.AccessCodeEntity;
import nl.davefemi.webchess.data.entity.GameSessionEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SessionStore implements GameSessionRepository {
    private final RedisTemplate<String, GameSessionEntity> sessionRedisTemplate;
    private final RedisTemplate<String, AccessCodeEntity> accessCodeRedisTemplate;
    private final int sessionTimeToLive = 5;

    @Override
    public GameSessionEntity retrieveGameSessionById(String sessionId) throws FileNotFoundException {
        GameSessionEntity sessionEntity = sessionRedisTemplate.opsForValue().get("session: " + sessionId);
        if (sessionEntity == null)
            throw new FileNotFoundException("Session not found");
        return sessionEntity;
    }

    @Override
    public void saveGameSession(GameSessionEntity entity) {
        sessionRedisTemplate.opsForValue().set("session: " + entity.getSessionId(), entity, Duration.of(sessionTimeToLive, ChronoUnit.MINUTES));
    }

    @Override
    public void saveAccessCode(AccessCodeEntity accessCode, int timeToLive){
        accessCodeRedisTemplate.opsForValue().set("access_code: " + accessCode.getToken(), accessCode, Duration.of(timeToLive, ChronoUnit.SECONDS));
    }

    @Override
    public AccessCodeEntity retrieveAccessCode(String code) throws FileNotFoundException {
        AccessCodeEntity accessCode = accessCodeRedisTemplate.opsForValue().get("access_code: " + code);
        deleteAccessCode(code);
        if (accessCode == null)
            throw new FileNotFoundException("Code not found");
        return accessCode;
    }

    private void deleteAccessCode(String code){
        accessCodeRedisTemplate.delete("access_code: " + code);
    }
}
