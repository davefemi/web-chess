package nl.davefemi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.data.dto.BoardDTO;
import nl.davefemi.data.dto.GameSessionDTO;
import nl.davefemi.data.dto.move.MoveDTO;
import nl.davefemi.data.dto.GameResponseDTO;
import nl.davefemi.data.mapper.*;
import nl.davefemi.data.mapper.move.MoveMapper;
import nl.davefemi.domain.game.Game;
import nl.davefemi.domain.game.actions.move.Move;
import nl.davefemi.domain.board.PlayerColor;
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
    private final GameResponseMapper gameResponseMapper;
    private final BoardMapper boardMapper;
    private final GameSessionMapper gameSessionMapper;
    private final GameSessionService gameSessionService;

    public BoardDTO getPositions(String gameId) throws FileNotFoundException {
        Game game = gameSessionService.collectGame(gameId);
        return boardMapper.mapDomainToDTO(game.getGameId(), game.getCopyOfBoard());
    }

    public GameResponseDTO getPlayerTurn(String gameId) throws GameException, FileNotFoundException {
        Game game = gameSessionService.collectGame(gameId);
        return gameResponseMapper.mapToDTO(game.getGameId(), game.getPlayerTurn().getColor(),  null);
    }

    public GameResponseDTO isCheck(String gameId, String color) throws BoardException, FileNotFoundException {
        Game game = gameSessionService.collectGame(gameId);
        return gameResponseMapper.mapToDTO(game.getGameId(), color,  game.isCheck(PlayerColor.fromString(color)));
    }

    public GameResponseDTO isCheckMate(String gameId, String color) throws BoardException, FileNotFoundException {
        Game game = gameSessionService.collectGame(gameId);
        return gameResponseMapper.mapToDTO(game.getGameId(), color,  game.isCheckMate(PlayerColor.fromString(color)));
    }

    public GameResponseDTO getAvailableMoves(String gameId, String color) throws BoardException, FileNotFoundException {
        Game game = gameSessionService.collectGame(gameId);
        List<Move> moves = game.getAvailableMoves(PlayerColor.fromString(color));
        return gameResponseMapper.mapToDTO(game.getGameId(), color, moveMapper.mapDomainToDTO(moves));
    }

    public GameSessionDTO executeMove(String gameId, String color, MoveDTO move) throws BoardException, MoveException, GameException, FileNotFoundException {
        Game game = gameSessionService.collectGame(gameId);
        game.executeMove(PlayerColor.fromString(color), moveMapper.mapDTOtoDomain(move));
        String playerColor = null;
        try {
            playerColor = game.getPlayerTurn().getColor();
        } catch (GameException e) {}
        gameSessionService.storeGame(game, playerColor);
        return gameSessionMapper.mapDomainToDTO(game, playerColor, "Move executed");
    }
}
