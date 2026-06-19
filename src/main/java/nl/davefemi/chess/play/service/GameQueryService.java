package nl.davefemi.chess.play.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.exception.SessionException;
import nl.davefemi.chess.http.response.game.GameStateDto;
import nl.davefemi.chess.http.response.session.SessionResponse;
import nl.davefemi.chess.data.mapper.session.BoardMapper;
import nl.davefemi.chess.data.mapper.session.GameStateMapper;
import nl.davefemi.chess.data.mapper.session.SessionResponseMapper;
import nl.davefemi.chess.data.mapper.move.MoveMapper;
import nl.davefemi.chess.exception.SessionNotFoundException;
import nl.davefemi.chess.play.model.game.Game;
import nl.davefemi.chess.play.model.actions.move.Move;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.GameException;
import nl.davefemi.chess.session.model.Player;
import nl.davefemi.chess.session.service.GameSessionService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameQueryService {
    private final MoveMapper moveMapper;
    private final SessionResponseMapper sessionResponseMapper;
    private final BoardMapper boardMapper;
    private final GameSessionService gameSessionService;
    private final GameStateMapper gameStateMapper;

    public SessionResponse getChessPositions(Player player)
            throws BoardException, SessionNotFoundException, SessionException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        return sessionResponseMapper.getSessionResponse(
                game.getSideToMove() == null
                ? null
                : game.getSideToMove().toString(),
                boardMapper.mapDomainToDTO(game.getGameBoardContext().getCopyOfBoard()));
    }

    public SessionResponse getPlayerTurn(Player player)
            throws BoardException, SessionNotFoundException, SessionException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        return sessionResponseMapper.getSessionResponse(game.getSideToMove().toString(),  null);
    }

    public SessionResponse isCheck(Player player)
            throws BoardException, SessionNotFoundException, SessionException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        return sessionResponseMapper.getSessionResponse(player.getColor().toString(), game.isCheck(player.getColor()));
    }

    public SessionResponse getAvailableMoves(Player player)
            throws BoardException, SessionNotFoundException, GameException, SessionException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        List<Move> moves = game.getAvailableMoves(player.getColor());
        return sessionResponseMapper.getSessionResponse(player.getColor().toString(), moveMapper.mapDomainToDTO(moves));
    }

    public GameStateDto getCurrentGameState(UUID sessionId)
            throws SessionNotFoundException, BoardException, SessionException {
        Game game = gameSessionService.getGameSession(sessionId).getCurrentGame();
        return gameStateMapper.mapDomainToDto(game);
    }
}
