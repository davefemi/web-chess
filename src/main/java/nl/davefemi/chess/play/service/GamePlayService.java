package nl.davefemi.chess.play.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.http.dto.MoveDto;
import nl.davefemi.chess.http.response.game.GameStateDto;
import nl.davefemi.chess.data.mapper.move.MoveMapper;
import nl.davefemi.chess.data.mapper.session.GameStateMapper;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.GameException;
import nl.davefemi.chess.exception.MoveException;
import nl.davefemi.chess.exception.SessionException;
import nl.davefemi.chess.http.websocket.service.event.MoveEvent;
import nl.davefemi.chess.play.model.game.Game;
import nl.davefemi.chess.session.model.GameSession;
import nl.davefemi.chess.session.model.Player;
import nl.davefemi.chess.session.service.GameSessionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GamePlayService {
    private final MoveMapper moveMapper;
    private final GameStateMapper gameStateMapper;
    private final GameSessionService gameSessionService;
    private final ApplicationEventPublisher publisher;

    public GameStateDto executeMove(Player player, MoveDto move)
            throws BoardException, MoveException, GameException, FileNotFoundException, SessionException {
        GameSession gameSession = gameSessionService.getGameSession(player.getSessionId());
        Game game = gameSession.getCurrentGame();
        if (game.getStatus().isActive()) {
            game.executeMove(player.getColor(), moveMapper.mapDTOtoDomain(move));
            gameSessionService.saveGameSession(gameSession);
            log.info("Executed sessionId={}, playerId={}, move {}", gameSession.getSessionId().toString(),
                    player.getId(), game.getLastMove().toString());
            publisher.publishEvent(new MoveEvent(gameSession.getSessionId(), game.getId(), player.getColor().toString()));
            return gameStateMapper.mapDomainToDto(game);
        }
        throw new GameException("Game is not active");
    }

    public GameStateDto surrender(Player player)
            throws FileNotFoundException, SessionException, BoardException, GameException {
        GameSession gameSession = gameSessionService.getGameSession(player.getSessionId());
        Game game = gameSession.getCurrentGame();
        if (game.getStatus().isActive()) {
            game.surrender(player.getColor());
            log.info("Executed sessionId={}, playerId={}: surrender request", player.getSessionId(), player.getId());
            gameSessionService.saveGameSession(gameSession);
            publisher.publishEvent(new MoveEvent(gameSession.getSessionId(), game.getId(), player.getColor().toString()));
            return gameStateMapper.mapDomainToDto(game);
        }
        throw new GameException("Game is not active");
    }
}
