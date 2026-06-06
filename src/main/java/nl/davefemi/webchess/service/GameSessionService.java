package nl.davefemi.webchess.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.dto.session.PlayerDTO;
import nl.davefemi.webchess.data.dto.session.SessionInvitationDTO;
import nl.davefemi.webchess.data.dto.session.SessionResponseDTO;
import nl.davefemi.webchess.data.entity.AccessCodeEntity;
import nl.davefemi.webchess.data.entity.GameSessionEntity;
import nl.davefemi.webchess.data.mapper.*;
import nl.davefemi.webchess.data.repository.GameSessionRepository;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.exception.UnauthorizedException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.session.AccessCodeGenerator;
import nl.davefemi.webchess.session.AccessCode;
import nl.davefemi.webchess.session.GameSession;
import nl.davefemi.webchess.session.Player;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameSessionService {
    private final GameSessionMapper gameSessionMapper;
    private final GameStateMapper gameStateMapper;
    private final GameSessionRepository sessionStore;
    private final SessionResponseMapper sessionResponseMapper;
    private final String inviteUrl = "/games/join?code=%s";
    private final AccessCodeMapper accessCodeMapper;
    private final PlayerMapper playerMapper;

    public SessionInvitationDTO startGameSession(String color) throws SessionException, BoardException {
        GameSession session = new GameSession();
        log.info("New session {} created", session.getSessionId());
        Player player = session.createPlayer(PieceColor.fromString(color));
        int timeToLive = 60;
        AccessCode accessCode = AccessCodeGenerator.getAccessCode(session.getSessionId().toString(), timeToLive);
        storeAccessCode(accessCode, timeToLive);
        storeSession(session);
        return sessionResponseMapper.mapInvitationToDTO(session, player, String.format(inviteUrl, accessCode.getToken()));
    }

    public SessionResponseDTO joinGameSession(String accessCode) throws SessionException, FileNotFoundException, BoardException, GameException {
        AccessCodeEntity code = retrieveAccessCode(accessCode);
        GameSession session =  retrieveSession(code.getSessionId());
        Player player = session.createPlayer();
        session.startSession();
        log.info("Session {} joined and started", session.getSessionId());
        storeSession(session);
        return sessionResponseMapper.mapToDTO(session, player.getPlayingColor().getColor(),"Successfully joined session");
    }

    public SessionResponseDTO endGame(String sessionId, Player player) throws FileNotFoundException, SessionException, BoardException {
        GameSession session = retrieveSession(sessionId);
        session.endSession(player);
        storeSession(session);
        return sessionResponseMapper.mapToDTO(session, null, "Session ended");
    }

    public SessionResponseDTO startRematch(String sessionId, PlayerDTO player) throws FileNotFoundException, BoardException, GameException, SessionException {
        GameSession session =  retrieveSession(sessionId);
        Game game = session.getRematch(playerMapper.mapDTOtoDomain(player));
        storeSession(session);
        return sessionResponseMapper.mapToDTO(session, null, gameStateMapper.mapDomainToDTO(game, game.getColorToMove().getColor()));
    }

    public SessionResponseDTO acceptRematch(String sessionId, PlayerDTO player) throws FileNotFoundException, SessionException, BoardException, GameException {
        GameSession session =  retrieveSession(sessionId);
        session.acceptRematch(playerMapper.mapDTOtoDomain(player));
        Game game = session.getCurrentGame();
        storeSession(session);
        return sessionResponseMapper.mapToDTO(session, null, gameStateMapper.mapDomainToDTO(game, game.getColorToMove().getColor()));
    }

    private GameSession retrieveSession(String sessionId) throws FileNotFoundException, SessionException, BoardException {
        return gameSessionMapper.mapEntityToDomain(sessionStore.retrieveGameSessionById(sessionId));
    }

    private AccessCodeEntity retrieveAccessCode(String accessCode) {
        try{
            return sessionStore.retrieveAccessCode(accessCode);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException("Access code has expired");
        }
    }

    private void storeSession(GameSession session) throws BoardException {
        GameSessionEntity sessionEntity = gameSessionMapper.mapDomainToEntity(session);
        sessionStore.saveGameSession(sessionEntity);
    }

    private void storeAccessCode(AccessCode code, int timeToLive){
        AccessCodeEntity accessCodeEntity = accessCodeMapper.mapToEntity(code);
        sessionStore.saveAccessCode(accessCodeEntity, timeToLive);
    }

    protected Pair<PieceColor, GameSession> getSessionAndPlayerColor(String playerId, String sessionId) throws FileNotFoundException, SessionException, BoardException {
        GameSession session = retrieveSession(sessionId);
        for (Player p : session.getPlayers()){
            if (p.getId().toString().equals(playerId)) {
                return Pair.of(p.getPlayingColor(), session);
            }
        }
        throw new SessionException("This player does not belong to this session");
    }

    protected GameSession getGameSession (String sessionId) throws FileNotFoundException, BoardException, SessionException {
        return retrieveSession(sessionId);
    }

    public void saveGameSession(GameSession gameSession) throws SessionException, BoardException {
        if (gameSession == null)
            throw new SessionException("No game session to store");
        storeSession(gameSession);
    }
}
