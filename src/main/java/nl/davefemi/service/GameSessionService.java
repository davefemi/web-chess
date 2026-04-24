package nl.davefemi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.data.dto.GameSessionDTO;
import nl.davefemi.data.entity.GameSessionEntity;
import nl.davefemi.data.mapper.GameSessionMapper;
import nl.davefemi.domain.game.Game;
import nl.davefemi.exception.GameException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameSessionService {
    private final RedisTemplate<String, GameSessionEntity> redisTemplate;
    private final GameSessionMapper gameSessionMapper;

    public GameSessionDTO getNewGame() throws GameException {
        Game game = new Game();
        GameSessionEntity session = gameSessionMapper.mapDomainToEntity(game, game.getPlayerTurn().getColor());
        redisTemplate.opsForValue().set("game: " + session.getGameId(), session);
        return gameSessionMapper.mapEntityToDTO(session, "Game initialised");
    }

    public Game getGame(UUID gameId) throws FileNotFoundException {
        return gameSessionMapper.mapEntityToDomain(redisTemplate.opsForValue().get("game: " + gameId.toString()));
    }

    public GameSessionDTO updateSession(Game game, String playerColor)  {
        GameSessionEntity session = gameSessionMapper.mapDomainToEntity(game, playerColor);
        redisTemplate.opsForValue().set(session.getGameId(), session);
        log.info(session.toString());
        return gameSessionMapper.mapEntityToDTO(session, "Move executed by " + playerColor);
    }
}
