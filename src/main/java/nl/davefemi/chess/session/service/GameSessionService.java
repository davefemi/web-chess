package nl.davefemi.chess.session.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.exception.*;
import nl.davefemi.chess.http.response.session.AcceptedSessionResponse;
import nl.davefemi.chess.http.response.session.EndedSessionResponse;
import nl.davefemi.chess.http.response.game.RequestedSessionResponse;
import nl.davefemi.chess.http.response.session.SessionResponse;
import nl.davefemi.chess.data.entity.session.GameSessionEntity;
import nl.davefemi.chess.data.mapper.session.*;
import nl.davefemi.chess.data.repository.SessionRepository;
import nl.davefemi.chess.http.websocket.event.*;
import nl.davefemi.chess.play.model.game.Game;
import nl.davefemi.chess.play.model.game.Color;
import nl.davefemi.chess.session.model.GameSession;
import nl.davefemi.chess.session.model.Player;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
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

    public RequestedSessionResponse startGameSession(String color) throws BoardException, InvalidTokenException, SessionException {
        GameSession session = new GameSession();
        log.info("Executed sessionId={}: session created", session.getSessionId());
        Player player = color == null
                ? session.addPlayer()
                : session.addPlayer(Color.fromString(color));
        String accessToken = credentialService.getAccessToken(session.getSessionId().toString());
        String playerToken = credentialService.getPlayerToken(player);
        storeSession(session);
        return sessionResponseMapper.getRequestedSessionResponse(
                player.getMessageEndpointId(),
                playerToken,
                player.getColor().toString(),
                accessToken);
    }

    public AcceptedSessionResponse joinGameSession(String accessToken)
            throws SessionNotFoundException, BoardException, GameException, InvalidTokenException, SessionException {
        GameSession session =  retrieveSession(credentialService.authenticateAccessToken(accessToken));
        Player player = session.addPlayer();
        String playerToken = credentialService.getPlayerToken(player);
        session.startSession();
        log.info("Executed sessionId={}: session joined and game started", session.getSessionId());
        storeSession(session);
        return sessionResponseMapper.getAcceptedSessionResponse(
                player.getMessageEndpointId(),
                playerToken,
                player.getColor().toString());
    }

    private EndedSessionResponse endSession(Player player)
            throws SessionNotFoundException, BoardException, SessionException {
        GameSession session = retrieveSession(player.getSessionId());
        session.endSession(player);
        storeSession(session);
        return sessionResponseMapper.getEndedSessionResponse(player.getColor().toString());
    }

    public SessionResponse offerRematch(Player player)
            throws BoardException, SessionNotFoundException, SessionException {
        GameSession session =  retrieveSession(player.getSessionId());
        Game game = session.getRematch(player);
        log.info("Executed sessionId={}: new game created", session.getSessionId());
        storeSession(session);
        applicationEventPublisher.publishEvent(new GameEvent<>(EventType.REMATCH_REQUESTED, session.getSessionId(), game.getId(), player));
        return sessionResponseMapper.getSessionResponse(player.getColor().toString(),
                gameStateMapper.mapDomainToDto(game));
    }

    public SessionResponse declineRematch(Player player)
            throws SessionNotFoundException, BoardException, SessionException {
        GameSession session =  retrieveSession(player.getSessionId());
        applicationEventPublisher.publishEvent(new GameEvent<>(EventType.REMATCH_DECLINED, session.getSessionId(), null, player));
        endSession(player);
        return null;
    }
    public SessionResponse acceptRematch(Player player)
            throws SessionNotFoundException, BoardException, GameException, SessionException {
        GameSession session =  retrieveSession(player.getSessionId());
        session.acceptRematch(player);
        Game game = session.getCurrentGame();
        log.info("Executed sessionId={}: new game started", session.getSessionId());
        storeSession(session);
        applicationEventPublisher.publishEvent(new GameEvent<>(EventType.REMATCH_ACCEPTED, session.getSessionId(), game.getId(), player));
        return sessionResponseMapper.getSessionResponse(player.getColor().toString(),
                    gameStateMapper.mapDomainToDto(game));
    }

    private GameSession retrieveSession(UUID sessionId)
            throws SessionNotFoundException, BoardException, SessionException {
        return gameSessionMapper.mapEntityToDomain(sessionStore.retrieveGameSessionById(sessionId.toString()));
    }

    private void storeSession(GameSession session) throws BoardException {
        GameSessionEntity sessionEntity = gameSessionMapper.mapDomainToEntity(session);
        log.info("Executed sessionId={}: session stored", session.getSessionId());
        sessionStore.saveGameSession(sessionEntity);
    }

    public GameSession getGameSession(UUID sessionId)
            throws BoardException, SessionNotFoundException, SessionException {
        GameSession session = retrieveSession(sessionId);
        log.info("Executed sessionId={}: session retrieval successful", session.getSessionId());
        return session;
    }

    public void saveGameSession(GameSession gameSession) throws SessionNotFoundException, BoardException {
        if (gameSession == null)
            throw new SessionNotFoundException("No game session to store");
        storeSession(gameSession);

    }
}
