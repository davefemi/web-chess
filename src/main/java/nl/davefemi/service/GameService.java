package nl.davefemi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.data.dto.BoardDTO;
import nl.davefemi.data.dto.GameSessionDTO;
import nl.davefemi.data.dto.move.MoveDTO;
import nl.davefemi.data.dto.GameReponseDTO;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameSessionService sessionService;
    private final MoveMapper moveMapper;
    private final GameResponseMapper gameResponseMapper;
    private final BoardMapper boardMapper;


    public BoardDTO getPositions(String gameId) throws FileNotFoundException {
        Game game = sessionService.getGame(UUID.fromString(gameId));
        return boardMapper.mapDomainToDTO(game.getGameId(), game.getCopyOfBoard());
    }

    public GameReponseDTO getPlayerTurn(String gameId) throws FileNotFoundException, GameException {
        Game game = sessionService.getGame(UUID.fromString(gameId));
        return gameResponseMapper.mapToDTO(game.getGameId(), game.getPlayerTurn().getColor(),  null);
    }

    public GameReponseDTO isCheck(String gameId, String color) throws FileNotFoundException, BoardException {
        Game game = sessionService.getGame(UUID.fromString(gameId));
        return gameResponseMapper.mapToDTO(game.getGameId(), color,  game.isCheck(PlayerColor.fromString(color)));
    }

    public GameReponseDTO isCheckMate(String gameId, String color) throws FileNotFoundException, BoardException {
        Game game = sessionService.getGame(UUID.fromString(gameId));
        return gameResponseMapper.mapToDTO(game.getGameId(), color,  game.isCheckMate(PlayerColor.fromString(color)));
    }

    public GameReponseDTO getAvailableMoves(String gameId, String color) throws FileNotFoundException, BoardException {
        Game game = sessionService.getGame(UUID.fromString(gameId));
        List<Move> moves = game.getAvailableMoves(PlayerColor.fromString(color));
        return gameResponseMapper.mapToDTO(game.getGameId(), color, moveMapper.mapDomainToDTO(moves));
    }

    public GameSessionDTO executeMove(String gameId, String color, MoveDTO move) throws FileNotFoundException, BoardException, MoveException, GameException {
        Game game = sessionService.getGame(UUID.fromString(gameId));
        game.executeMove(PlayerColor.fromString(color), moveMapper.mapDTOtoDomain(move));
        String playerColor = null;
        try {
            playerColor = game.getPlayerTurn().getColor();
        } catch (GameException e) {}
        return sessionService.updateSession(game, playerColor);
    }

}
