package nl.davefemi.chess.play.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.data.dto.session.GameStateDTO;
import nl.davefemi.chess.data.dto.session.SessionResponseDTO;
import nl.davefemi.chess.data.mapper.session.BoardMapper;
import nl.davefemi.chess.data.mapper.session.GameStateMapper;
import nl.davefemi.chess.data.mapper.session.SessionResponseMapper;
import nl.davefemi.chess.data.mapper.move.MoveMapper;
import nl.davefemi.chess.exception.SessionException;
import nl.davefemi.chess.play.model.game.Game;
import nl.davefemi.chess.play.model.actions.move.Move;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.exception.GameException;
import nl.davefemi.chess.session.model.Player;
import nl.davefemi.chess.session.service.GameSessionService;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameQueryService {
    private final MoveMapper moveMapper;
    private final SessionResponseMapper sessionResponseMapper;
    private final BoardMapper boardMapper;
    private final GameSessionService gameSessionService;
    private final GameStateMapper gameStateMapper;

    public SessionResponseDTO getChessPositions(Player player)
            throws FileNotFoundException, BoardException, GameException, SessionException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        return sessionResponseMapper.mapToDTO(
                game.getSideToMove() == null
                ? null
                : game.getSideToMove().toString(),
                boardMapper.mapDomainToDTO(game.getGameBoardContext().getCopyOfBoard()));
    }

    public SessionResponseDTO getPlayerTurn(Player player)
            throws GameException, FileNotFoundException, BoardException, SessionException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        return sessionResponseMapper.mapToDTO(game.getSideToMove().toString(),  null);
    }

    public SessionResponseDTO isCheck(Player player)
            throws FileNotFoundException, BoardException, SessionException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        return sessionResponseMapper.mapToDTO(player.getColor().toString(), game.isCheck(player.getColor()));
    }

    public SessionResponseDTO getAvailableMoves(Player player)
            throws BoardException, FileNotFoundException, SessionException, GameException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        List<Move> moves = game.getAvailableMoves(player.getColor());
        return sessionResponseMapper.mapToDTO(player.getColor().toString(), moveMapper.mapDomainToDTO(moves));
    }

    public GameStateDTO getGameState(Player player)
            throws FileNotFoundException, SessionException, BoardException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        return gameStateMapper.mapDomainToDTO(game);
    }
}
