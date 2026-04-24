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
    public GameSessionEntity retrieveGameByGameId(String gameId) throws FileNotFoundException {
        GameSessionEntity sessionEntity = redisTemplate.opsForValue().get("game: " + gameId.toString());
        if (sessionEntity == null)
            throw new FileNotFoundException("Game not found");
        return sessionEntity;
    }

    @Override
    public void saveGame(GameSessionEntity game) {
        redisTemplate.opsForValue().set("game: " + game.getGameId(), game, Duration.of(timeToLive, ChronoUnit.SECONDS));
    }
}
