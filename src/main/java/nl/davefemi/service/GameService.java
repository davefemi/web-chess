package nl.davefemi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.data.dto.GameStateDTO;
import nl.davefemi.data.dto.move.MoveDTO;
import nl.davefemi.data.dto.SessionResponseDTO;
import nl.davefemi.data.mapper.*;
import nl.davefemi.data.mapper.move.MoveMapper;
import nl.davefemi.exception.SessionException;
import nl.davefemi.game.Game;
import nl.davefemi.game.actions.Move;
import nl.davefemi.game.board.PieceColor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.exception.GameException;
import nl.davefemi.exception.MoveException;
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
        Game game = gameSessionService.getGame(sessionId);
        return sessionResponseMapper.mapToDTO(sessionId, game.getPlayerTurn().getColor(), boardMapper.mapDomainToDTO(game.getCopyOfBoard()));
    }

    public SessionResponseDTO getPlayerTurn(String sessionId) throws GameException, FileNotFoundException, BoardException, SessionException {
        Game game = gameSessionService.getGame(sessionId);
        return sessionResponseMapper.mapToDTO(sessionId, game.getPlayerTurn().getColor(),  null);
    }

    public SessionResponseDTO getStatus(String sessionId, String color) throws FileNotFoundException, BoardException, SessionException {
        Game game = gameSessionService.getGame(sessionId);
        return sessionResponseMapper.mapToDTO(sessionId, color, game.getStatus(PieceColor.fromString(color)).getStatus());
    }

    public SessionResponseDTO getAvailableMoves(String sessionId, String color) throws BoardException, FileNotFoundException, SessionException {
        Game game = gameSessionService.getGame(sessionId);
        List<Move> moves = game.getAvailableMoves(PieceColor.fromString(color));
        return sessionResponseMapper.mapToDTO(sessionId, color, moveMapper.mapDomainToDTO(moves));
    }

    public GameStateDTO executeMove(String sessionId, String color, MoveDTO move) throws BoardException, MoveException, GameException, FileNotFoundException, SessionException {
        Game game = gameSessionService.getGame(sessionId);
        game.executeMove(PieceColor.fromString(color), moveMapper.mapDTOtoDomain(move));
        String playerColor = null;
        try {
            playerColor = game.getPlayerTurn().getColor();
        } catch (GameException e) {}
        gameSessionService.saveGame(game);
        return gameStateMapper.mapDomainToDTO(game, playerColor);
    }
}
