package nl.davefemi.service;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.SessionResponseDTO;
import nl.davefemi.data.entity.GameSessionEntity;
import nl.davefemi.data.mapper.GameSessionMapper;
import nl.davefemi.data.mapper.GameStateMapper;
import nl.davefemi.data.mapper.SessionResponseMapper;
import nl.davefemi.data.repository.GameSessionRepository;
import nl.davefemi.exception.SessionException;
import nl.davefemi.game.Game;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.game.board.PieceColor;
import nl.davefemi.session.GameSession;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GameSessionService {
    private final GameSessionMapper gameSessionMapper;
    private final GameStateMapper gameStateMapper;
    private final GameSessionRepository sessionStore;
    private final SessionResponseMapper sessionResponseMapper;
    private final ConcurrentHashMap<Game, GameSession> gameSessions = new ConcurrentHashMap<>();

    public SessionResponseDTO startGameSession(String color) throws GameException, SessionException {
        GameSession session = new GameSession();
        session.createPlayer(PieceColor.fromString(color));
        Game game = session.getCurrentGame();
        storeSession(session);
        return sessionResponseMapper.mapToDTO(session, null, gameStateMapper.mapDomainToDTO(game, game.getPlayerTurn().getColor()));
    }

    public SessionResponseDTO joinGameSession(String sessionId, String color) throws GameException, SessionException, FileNotFoundException, BoardException {
        GameSession session =  retrieveSession(sessionId);
        session.createPlayer(PieceColor.fromString(color));
        Game game = session.getCurrentGame();
        storeSession(session);
        return sessionResponseMapper.mapToDTO(session, null, gameStateMapper.mapDomainToDTO(game, game.getPlayerTurn().getColor()));
    }


    public SessionResponseDTO startNewGameInCurrentSession(String sessionId) throws FileNotFoundException, SessionException, BoardException, GameException {
        GameSession session =  retrieveSession(sessionId);
        if (session.getCurrentGame().isGameActive())
            throw new GameException("End current game first");
        Game game = session.startNewGame();
        storeSession(session);
        return sessionResponseMapper.mapToDTO(session, null, gameStateMapper.mapDomainToDTO(game, game.getPlayerTurn().getColor()));
    }

    private GameSession retrieveSession(String sessionId) throws FileNotFoundException, SessionException, BoardException {
        return gameSessionMapper.mapEntityToDomain(sessionStore.retrieveGameSessionById(sessionId));
    }

    private void storeSession(GameSession session){
        GameSessionEntity sessionEntity = gameSessionMapper.mapDomainToEntity(session);
        sessionStore.saveGameSession(sessionEntity);
    }

    protected Game getGame(String sessionId) throws FileNotFoundException, BoardException, SessionException {
        GameSession session = retrieveSession(sessionId);
        cacheGame(session);
        return session.getCurrentGame();
    }

    private void cacheGame(GameSession session){
        if(!gameSessions.contains(session))
            gameSessions.put(session.getCurrentGame(), session);
    }

    public void saveGame(Game game) throws SessionException {
        GameSession session = gameSessions.remove(game);
        if (session == null) {
            throw new SessionException("Could not store game");
        }
        storeSession(session);
    }

}
