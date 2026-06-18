package nl.davefemi.webchess.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.dto.MoveDTO;
import nl.davefemi.webchess.data.dto.session.GameStateDTO;
import nl.davefemi.webchess.data.mapper.move.MoveMapper;
import nl.davefemi.webchess.data.mapper.session.GameStateMapper;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.exception.MoveException;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.session.GameSession;
import nl.davefemi.webchess.session.Player;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GamePlayService {
    private final MoveMapper moveMapper;
    private final GameStateMapper gameStateMapper;
    private final GameSessionService gameSessionService;
    private final GameMessageService gameMessageService;

    public GameStateDTO executeMove(Player player, MoveDTO move)
            throws BoardException, MoveException, GameException, FileNotFoundException, SessionException {
        GameSession gameSession = gameSessionService.getGameSession(player.getSessionId());
        Game game = gameSession.getCurrentGame();
        if (game.getStatus().isActive()) {
            game.executeMove(player.getColor(), moveMapper.mapDTOtoDomain(move));
            String playerColor = null;
            try {
                playerColor = game.getSideToMove().toString();
            } catch (NullPointerException | GameException e) {
            }
            gameSessionService.saveGameSession(gameSession);
            log.info("Executed sessionId={}, playerId={}, move {}", gameSession.getSessionId().toString(),
                    player.getId(), game.getLastMove().toString());
            gameMessageService.sendGameState(gameSession.getSubscriptionId(),
                    gameStateMapper.mapDomainToDTO(game, playerColor));
            return gameStateMapper.mapDomainToDTO(game, playerColor);
        }
        throw new GameException("Game is not active");
    }

    public GameStateDTO surrender(Player player)
            throws FileNotFoundException, SessionException, BoardException, GameException {
        GameSession gameSession = gameSessionService.getGameSession(player.getSessionId());
        Game game = gameSession.getCurrentGame();
        if (game.getStatus().isActive()) {
            game.surrender(player.getColor());
            String playerColor = null;
            try {
                playerColor = game.getSideToMove().toString();
            } catch (NullPointerException | GameException e) {
            }
            log.info("Executed sessionId={}, playerId={}: surrender request", player.getSessionId(), player.getId());
            gameSessionService.saveGameSession(gameSession);
            return gameStateMapper.mapDomainToDTO(game, playerColor);
        }
        throw new GameException("Game is not active");
    }
}
