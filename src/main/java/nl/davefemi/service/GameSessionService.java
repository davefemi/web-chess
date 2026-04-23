package nl.davefemi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.data.dto.GameSessionDTO;
import nl.davefemi.data.entity.GameSessionEntity;
import nl.davefemi.data.mapper.GameSessionMapper;
import nl.davefemi.data.repository.GameSessionRepository;
import nl.davefemi.domain.game.Game;
import nl.davefemi.exception.GameException;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameSessionService {
    private final GameSessionMapper gameSessionMapper;
    private final GameSessionRepository gameSessionRepository;

    public GameSessionDTO getNewGame() throws GameException {
        Game game = new Game();
        GameSessionEntity session = gameSessionMapper.mapDomainToEntity(game, game.getPlayerTurn().getColor());
        gameSessionRepository.save(session);
        return gameSessionMapper.mapEntityToDTO(session, "Game initialised");
    }

    public Game getGame(UUID gameId) throws FileNotFoundException {
        Optional<GameSessionEntity> session = gameSessionRepository.findById(gameId.toString());
        return gameSessionMapper.mapEntityToDomain(session.orElseThrow(()->new FileNotFoundException("Game not found")));
    }

    public GameSessionDTO updateSession(Game game, String playerColor)  {
        GameSessionEntity session = gameSessionMapper.mapDomainToEntity(game, playerColor);
        gameSessionRepository.save(session);
        log.info(session.toString());
        return gameSessionMapper.mapEntityToDTO(session, "Move executed by " + playerColor);
    }
}
