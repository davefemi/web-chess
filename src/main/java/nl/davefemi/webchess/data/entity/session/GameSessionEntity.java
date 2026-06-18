package nl.davefemi.webchess.data.entity.session;

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
    private String subscriptionId;
    private boolean isActiveSession;
    private List<PlayerEntity> players = new ArrayList<>();
    private List<GameStateEntity> games = new ArrayList<>();
    private  PlayerEntity playerToAccept;
}
