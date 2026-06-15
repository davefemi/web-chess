package nl.davefemi.webchess.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.dto.session.SessionResponseDTO;
import nl.davefemi.webchess.data.mapper.session.BoardMapper;
import nl.davefemi.webchess.data.mapper.session.SessionResponseMapper;
import nl.davefemi.webchess.data.mapper.move.MoveMapper;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.game.actions.move.Move;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.session.Player;
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

    public SessionResponseDTO getChessPositions(Player player)
            throws FileNotFoundException, BoardException, GameException, SessionException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        return sessionResponseMapper.mapToDTO(
                game.getSideToMove() == null
                ? null
                : game.getSideToMove().getColor(),
                boardMapper.mapDomainToDTO(game.getGameBoardContext().getCopyOfBoard()));
    }

    public SessionResponseDTO getPlayerTurn(Player player)
            throws GameException, FileNotFoundException, BoardException, SessionException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        return sessionResponseMapper.mapToDTO(game.getSideToMove().getColor(),  null);
    }

    public SessionResponseDTO isCheck(Player player)
            throws FileNotFoundException, BoardException, SessionException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        return sessionResponseMapper.mapToDTO(player.getColor().getColor(), game.isCheck(player.getColor()));
    }

    public SessionResponseDTO getAvailableMoves(Player player)
            throws BoardException, FileNotFoundException, SessionException, GameException {
        Game game = gameSessionService.getGameSession(player.getSessionId()).getCurrentGame();
        List<Move> moves = game.getAvailableMoves(player.getColor());
        return sessionResponseMapper.mapToDTO(player.getColor().getColor(), moveMapper.mapDomainToDTO(moves));
    }

}
