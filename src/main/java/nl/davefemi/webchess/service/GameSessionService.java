package nl.davefemi.webchess.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.dto.session.SessionInitiationDTO;
import nl.davefemi.webchess.data.dto.session.SessionResponseDTO;
import nl.davefemi.webchess.data.entity.session.GameSessionEntity;
import nl.davefemi.webchess.data.mapper.session.*;
import nl.davefemi.webchess.data.repository.SessionRepository;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.session.GameSession;
import nl.davefemi.webchess.session.Player;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameSessionService {
    private final GameSessionMapper gameSessionMapper;
    private final GameStateMapper gameStateMapper;
    private final SessionRepository sessionStore;
    private final SessionResponseMapper sessionResponseMapper;
    private static final String INVITE_URL = "/games/join?token=%s";
    private final TokenService tokenService;

    public SessionInitiationDTO startGameSession(String color) throws SessionException, BoardException {
        GameSession session = new GameSession();
        log.info("Executed sessionId={}: session created", session.getSessionId());
        Player player = session.addPlayer(Color.fromString(color));
        String accessToken = tokenService.generateAccessToken(session.getSessionId().toString());
        String playerToken = tokenService.generatePlayerToken(player);
        storeSession(session);
        return sessionResponseMapper.mapInvitationToDTO(playerToken, player.getColor().getColor(), String.format(INVITE_URL, accessToken));
    }

    public SessionInitiationDTO joinGameSession(String accessToken)
            throws SessionException, FileNotFoundException, BoardException, GameException {
        GameSession session =  retrieveSession(tokenService.authenticateAccessToken(accessToken));
        Player player = session.addPlayer();
        String playerToken = tokenService.generatePlayerToken(player);
        session.startSession();
        log.info("Executed sessionId={}: session joined and game started", session.getSessionId());
        storeSession(session);
        return sessionResponseMapper.mapInvitationToDTO(playerToken, player.getColor().getColor(),
                "Successfully joined session");
    }

    public SessionResponseDTO endSession(Player player)
            throws FileNotFoundException, SessionException, BoardException {
        GameSession session = retrieveSession(player.getSessionId());
        session.endSession(player);
        storeSession(session);
        return sessionResponseMapper.mapToDTO( player.getColor().getColor(), "Session ended");
    }

    public SessionResponseDTO startRematch(Player player)
            throws FileNotFoundException, BoardException, GameException, SessionException {
        GameSession session =  retrieveSession(player.getSessionId());
        Game game = session.getRematch(player);
        log.info("Executed sessionId={}: new game created", session.getSessionId());
        storeSession(session);
        return sessionResponseMapper.mapToDTO(player.getColor().getColor(),
                gameStateMapper.mapDomainToDTO(game, game.getSideToMove().getColor()));
    }

    public SessionResponseDTO acceptRematch(Player player)
            throws FileNotFoundException, SessionException, BoardException, GameException {
        GameSession session =  retrieveSession(player.getSessionId());
        session.acceptRematch(player);
        Game game = session.getCurrentGame();
        log.info("Executed sessionId={}: new game started", session.getSessionId());
        storeSession(session);
        return sessionResponseMapper.mapToDTO(player.getColor().getColor(),
                gameStateMapper.mapDomainToDTO(game, game.getSideToMove().getColor()));
    }

    private GameSession retrieveSession(UUID sessionId)
            throws FileNotFoundException, SessionException, BoardException {
        return gameSessionMapper.mapEntityToDomain(sessionStore.retrieveGameSessionById(sessionId.toString()));
    }

    private void storeSession(GameSession session) throws BoardException {
        GameSessionEntity sessionEntity = gameSessionMapper.mapDomainToEntity(session);
        log.info("Executed sessionId={}: session stored", session.getSessionId());
        sessionStore.saveGameSession(sessionEntity);
    }

    protected GameSession getGameSession (UUID sessionId)
            throws FileNotFoundException, BoardException, SessionException {
        GameSession session = retrieveSession(sessionId);
        log.info("Executed sessionId={}: session retrieval successful", session.getSessionId());
        return session;
    }

    public void saveGameSession(GameSession gameSession) throws SessionException, BoardException {
        if (gameSession == null)
            throw new SessionException("No game session to store");
        storeSession(gameSession);

    }
}
