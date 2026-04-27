package nl.davefemi.webchess.service;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.dto.session.SessionInvitationDTO;
import nl.davefemi.webchess.data.dto.session.SessionResponseDTO;
import nl.davefemi.webchess.data.entity.AccessCodeEntity;
import nl.davefemi.webchess.data.entity.GameSessionEntity;
import nl.davefemi.webchess.data.mapper.AccessCodeMapper;
import nl.davefemi.webchess.data.mapper.GameSessionMapper;
import nl.davefemi.webchess.data.mapper.GameStateMapper;
import nl.davefemi.webchess.data.mapper.SessionResponseMapper;
import nl.davefemi.webchess.data.repository.GameSessionRepository;
import nl.davefemi.webchess.exception.SessionException;
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

@Service
@RequiredArgsConstructor
public class GameSessionService {
    private final GameSessionMapper gameSessionMapper;
    private final GameStateMapper gameStateMapper;
    private final GameSessionRepository sessionStore;
    private final SessionResponseMapper sessionResponseMapper;
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

    public SessionResponseDTO joinGameSession(String accessCode) throws SessionException, FileNotFoundException, BoardException {
        AccessCodeEntity code = retrieveAccessCode(accessCode);
        GameSession session =  retrieveSession(code.getSessionId());
        Player player = session.createPlayer();
        session.startSession();
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

    protected Pair<PieceColor, GameSession> getSessionAndPlayerColor(String playerId, String sessionId) throws FileNotFoundException, SessionException, BoardException {
        GameSession session = retrieveSession(sessionId);
        for (Player p : session.getPlayers()){
            if (p.getId().toString().equals(playerId)) {
                return Pair.of(p.getPlayerColor(), session);
            }
        }
        throw new SessionException("This player does not belong to this session");
    }

    protected GameSession getGameSession (String sessionId) throws FileNotFoundException, BoardException, SessionException {
        return retrieveSession(sessionId);
    }

    public void saveGameSession(GameSession gameSession) throws SessionException {
        if (gameSession == null)
            throw new SessionException("No game session to store");
        storeSession(gameSession);
    }
}
