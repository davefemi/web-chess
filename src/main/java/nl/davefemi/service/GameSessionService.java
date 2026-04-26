package nl.davefemi.service;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.dto.session.SessionInvitationDTO;
import nl.davefemi.data.dto.session.SessionResponseDTO;
import nl.davefemi.data.entity.AccessCodeEntity;
import nl.davefemi.data.entity.GameSessionEntity;
import nl.davefemi.data.mapper.AccessCodeMapper;
import nl.davefemi.data.mapper.GameSessionMapper;
import nl.davefemi.data.mapper.GameStateMapper;
import nl.davefemi.data.mapper.SessionResponseMapper;
import nl.davefemi.data.repository.GameSessionRepository;
import nl.davefemi.exception.SessionException;
import nl.davefemi.game.Game;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.game.board.PieceColor;
import nl.davefemi.session.AccessCodeGenerator;
import nl.davefemi.session.AccessCode;
import nl.davefemi.session.GameSession;
import nl.davefemi.session.Player;
import org.springframework.data.util.Pair;
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
    private final String inviteUrl = "/games/join?code=%s";
    private final AccessCodeMapper accessCodeMapper;

    public SessionInvitationDTO startGameSession(String color) throws SessionException {
        GameSession session = new GameSession();
        Player player = session.createPlayer(PieceColor.fromString(color));
        int timeToLive = 60;
        AccessCode accessCode = AccessCodeGenerator.getAccessCode(session.getSessionId().toString(), timeToLive);
        storeAccessCode(accessCode, timeToLive);
        storeSession(session);
        return sessionResponseMapper.mapInvitationToDTO(session, player, String.format(inviteUrl, accessCode.getToken()));
    }

    public SessionResponseDTO joinGameSession(String accessCode) throws GameException, SessionException, FileNotFoundException, BoardException {
        AccessCodeEntity code = retrieveAccessCode(accessCode);
        GameSession session =  retrieveSession(code.getSessionId());
        Player player = session.createPlayer();
        storeSession(session);
        return sessionResponseMapper.mapToDTO(session, player.getPlayerColor().getColor(),"Successfully joined session");
    }

    public SessionResponseDTO endGame(String sessionId) throws FileNotFoundException, SessionException, BoardException {
        GameSession session = retrieveSession(sessionId);
        session.endCurrentGame();
        storeSession(session);
        return sessionResponseMapper.mapToDTO(session, null, "Session ended");
    }

    public SessionResponseDTO startNewGameInCurrentSession(String sessionId) throws FileNotFoundException, BoardException, GameException, SessionException {
        GameSession session =  retrieveSession(sessionId);
        Game game = session.startNewGame();
        storeSession(session);
        return sessionResponseMapper.mapToDTO(session, null, gameStateMapper.mapDomainToDTO(game, game.getPlayerTurn().getColor()));
    }

    private GameSession retrieveSession(String sessionId) throws FileNotFoundException, SessionException, BoardException {
        return gameSessionMapper.mapEntityToDomain(sessionStore.retrieveGameSessionById(sessionId));
    }

    private AccessCodeEntity retrieveAccessCode(String accessCode) throws SessionException {
        try{
            return sessionStore.retrieveAccessCode(accessCode);
        } catch (FileNotFoundException e) {
            throw new SessionException("Access code has expired");
        }
    }

    private void storeSession(GameSession session){
        GameSessionEntity sessionEntity = gameSessionMapper.mapDomainToEntity(session);
        sessionStore.saveGameSession(sessionEntity);
    }

    private void storeAccessCode(AccessCode code, int timeToLive){
        AccessCodeEntity accessCodeEntity = accessCodeMapper.mapToEntity(code);
        sessionStore.saveAccessCode(accessCodeEntity, timeToLive);
    }

    protected Pair<PieceColor, Game> getGameAndPlayerColor(String playerId, String sessionId) throws FileNotFoundException, SessionException, BoardException {
        GameSession session = retrieveSession(sessionId);
        for (Player p : session.getPlayers()){
            if (p.getId().toString().equals(playerId)) {
                cacheGame(session);
                return Pair.of(p.getPlayerColor(), session.getCurrentGame());
            }
        }
        throw new SessionException("This player does not belong to this session");
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
