package nl.davefemi.data.entity;

import lombok.Data;
import nl.davefemi.domain.game.move.MoveRecord;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.ArrayList;
import java.util.List;

@RedisHash(value = "game-session", timeToLive = 300)
@Data
@EnableRedisRepositories
public class GameSession {

    @Id
    private String gameId;
    private String turn;
    private boolean status;
    private final List<MoveRecord> moves = new ArrayList<>();

}
