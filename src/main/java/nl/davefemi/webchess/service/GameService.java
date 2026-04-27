package nl.davefemi.webchess.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.dto.GameStateDTO;
import nl.davefemi.webchess.data.dto.move.MoveDTO;
import nl.davefemi.webchess.data.dto.session.SessionResponseDTO;
import nl.davefemi.webchess.data.mapper.BoardMapper;
import nl.davefemi.webchess.data.mapper.GameStateMapper;
import nl.davefemi.webchess.data.mapper.SessionResponseMapper;
import nl.davefemi.webchess.data.mapper.move.MoveMapper;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.actions.Move;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.session.GameSession;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final MoveMapper moveMapper;
    private final SessionResponseMapper sessionResponseMapper;
    private final BoardMapper boardMapper;
    private final GameStateMapper gameStateMapper;
    private final GameSessionService gameSessionService;

    public SessionResponseDTO getChessPositions(String sessionId) throws FileNotFoundException, BoardException, GameException, SessionException {
        Game game = gameSessionService.getGameSession(sessionId).getCurrentGame();
        return sessionResponseMapper.mapToDTO(sessionId, game.getPlayerTurn().getColor(), boardMapper.mapDomainToDTO(game.getCopyOfBoard()));
    }

    public SessionResponseDTO getPlayerTurn(String sessionId) throws GameException, FileNotFoundException, BoardException, SessionException {
        Game game = gameSessionService.getGameSession(sessionId).getCurrentGame();
        return sessionResponseMapper.mapToDTO(sessionId, game.getPlayerTurn().getColor(),  null);
    }

    public SessionResponseDTO getStatus(String sessionId, String color) throws FileNotFoundException, BoardException, SessionException {
        Game game = gameSessionService.getGameSession(sessionId).getCurrentGame();
        return sessionResponseMapper.mapToDTO(sessionId, color, game.getStatus(PieceColor.fromString(color)).getStatus());
    }

    public SessionResponseDTO getAvailableMoves(String sessionId, String color) throws BoardException, FileNotFoundException, SessionException {
        Game game = gameSessionService.getGameSession(sessionId).getCurrentGame();
        List<Move> moves = game.getAvailableMoves(PieceColor.fromString(color));
        return sessionResponseMapper.mapToDTO(sessionId, color, moveMapper.mapDomainToDTO(moves));
    }

    public GameStateDTO executeMove(String playerId, String sessionId, MoveDTO move) throws BoardException, MoveException, GameException, FileNotFoundException, SessionException {
        Pair<PieceColor, GameSession> gamePair = gameSessionService.getSessionAndPlayerColor(playerId, sessionId);
        GameSession gameSession = gamePair.getSecond();
        Game game = gameSession.getCurrentGame();
        if (game.isGameActive()) {
            game.executeMove(gamePair.getFirst(), moveMapper.mapDTOtoDomain(move));
            String playerColor = null;
            try {
                playerColor = game.getPlayerTurn().getColor();
            } catch (GameException e) {
            }
            gameSessionService.saveGameSession(gameSession);
            log.info("SessionId=" + gameSession.getSessionId().toString() + " {}", game.getLastMove().toString());
            return gameStateMapper.mapDomainToDTO(game, playerColor);
        }
        throw new GameException("Game is not active");
    }
}
