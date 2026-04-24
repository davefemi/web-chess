package nl.davefemi.service;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.GameSessionDTO;
import nl.davefemi.data.entity.GameSessionEntity;
import nl.davefemi.data.mapper.GameSessionMapper;
import nl.davefemi.data.repository.GameSessionRepository;
import nl.davefemi.domain.game.Game;
import nl.davefemi.exception.GameException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
@RequiredArgsConstructor
public class GameSessionService {
    private final GameSessionMapper gameSessionMapper;
    private final GameSessionRepository sessionStore;

    public GameSessionDTO getNewGame() throws GameException {
        Game game = new Game();
        GameSessionEntity session = gameSessionMapper.mapDomainToEntity(game, game.getPlayerTurn().getColor());
        sessionStore.saveGame(session);
        return gameSessionMapper.mapEntityToDTO(session, "Game initialised");
    }

    public Game collectGame(String gameId) throws FileNotFoundException {
        return gameSessionMapper.mapEntityToDomain(sessionStore.retrieveGameByGameId(gameId));
    }

    public void storeGame(Game game, String playerColor){
        GameSessionEntity session = gameSessionMapper.mapDomainToEntity(game, playerColor);
        sessionStore.saveGame(session);
    }

}
