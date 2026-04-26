package nl.davefemi.data.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import java.util.ArrayList;
import java.util.List;

@RedisHash(value = "game-session", timeToLive = 300)
@Data
public class GameSessionEntity {

    @Id
    private String sessionId;
    private boolean isActive;
    private List<PlayerEntity> players = new ArrayList<>();
    private List<GameStateEntity> games = new ArrayList<>();
}
