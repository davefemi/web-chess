package nl.davefemi.chess.session.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.http.response.session.AcceptedSessionResponse;
import nl.davefemi.chess.http.response.session.EndedSessionResponse;
import nl.davefemi.chess.http.response.game.RequestedSessionResponse;
import nl.davefemi.chess.http.response.session.SessionResponse;
import nl.davefemi.chess.data.entity.session.GameSessionEntity;
import nl.davefemi.chess.data.mapper.session.*;
import nl.davefemi.chess.data.repository.SessionRepository;
import nl.davefemi.chess.exception.InvalidTokenException;
import nl.davefemi.chess.exception.SessionException;
import nl.davefemi.chess.http.websocket.event.RematchAcceptedEvent;
import nl.davefemi.chess.http.websocket.event.RematchDeclinedEvent;
import nl.davefemi.chess.http.websocket.event.RematchRequestEvent;
import nl.davefemi.chess.play.model.game.Game;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.GameException;
import nl.davefemi.chess.play.model.game.Color;
import nl.davefemi.chess.session.model.GameSession;
import nl.davefemi.chess.session.model.Player;
import org.springframework.context.ApplicationEventPublisher;
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
    private final CredentialService credentialService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public RequestedSessionResponse startGameSession(String color) throws SessionException, BoardException, InvalidTokenException {
        GameSession session = new GameSession();
        log.info("Executed sessionId={}: session created", session.getSessionId());
        Player player = color == null
                ? session.addPlayer()
                : session.addPlayer(Color.fromString(color));
        String accessToken = credentialService.getAccessToken(session.getSessionId().toString());
        String playerToken = credentialService.getPlayerToken(player);
        storeSession(session);
        return sessionResponseMapper.getRequestedSessionResponse(
                session.getCurrentGame().getId(),
                player.getMessageId(),
                playerToken,
                player.getColor().toString(),
                accessToken);
    }

    public AcceptedSessionResponse joinGameSession(String accessToken)
            throws SessionException, FileNotFoundException, BoardException, GameException, InvalidTokenException {
        GameSession session =  retrieveSession(credentialService.authenticateAccessToken(accessToken));
        Player player = session.addPlayer();
        String playerToken = credentialService.getPlayerToken(player);
        session.startSession();
        log.info("Executed sessionId={}: session joined and game started", session.getSessionId());
        storeSession(session);
        return sessionResponseMapper.getAcceptedSessionResponse(
                session.getCurrentGame().getId(),
                player.getMessageId(),
                playerToken,
                player.getColor().toString());
    }

    public EndedSessionResponse endSession(Player player)
            throws FileNotFoundException, SessionException, BoardException {
        GameSession session = retrieveSession(player.getSessionId());
        session.endSession(player);
        storeSession(session);
        return sessionResponseMapper.getEndedSessionResponse(player.getColor().toString());
    }

    public SessionResponse startRematch(Player player)
            throws FileNotFoundException, BoardException, SessionException {
        GameSession session =  retrieveSession(player.getSessionId());
        Game game = session.getRematch(player);
        log.info("Executed sessionId={}: new game created", session.getSessionId());
        storeSession(session);
        applicationEventPublisher.publishEvent(new RematchRequestEvent(session.getSessionId(), game.getId(), player.getColor().toString()));
        return sessionResponseMapper.getSessionResponse(player.getColor().toString(),
                gameStateMapper.mapDomainToDto(game));
    }

    public SessionResponse acceptRematch(Player player, boolean accepted)
            throws FileNotFoundException, SessionException, BoardException, GameException {
        GameSession session =  retrieveSession(player.getSessionId());
        if (accepted) {
            session.acceptRematch(player);
            Game game = session.getCurrentGame();
            log.info("Executed sessionId={}: new game started", session.getSessionId());
            storeSession(session);
            applicationEventPublisher.publishEvent(new RematchAcceptedEvent(session.getSessionId(), game.getId(), player.getColor().toString()));
            return sessionResponseMapper.getSessionResponse(player.getColor().toString(),
                    gameStateMapper.mapDomainToDto(game));
        }
        applicationEventPublisher.publishEvent(new RematchDeclinedEvent(session.getSessionId(), player.getColor().toString()));
        return null;
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

    public GameSession getGameSession(UUID sessionId)
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
