package nl.davefemi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.data.dto.*;
import nl.davefemi.data.entity.GameSession;
import nl.davefemi.data.mapper.*;
import nl.davefemi.data.repository.GameSessionRepository;
import nl.davefemi.domain.game.Game;
import nl.davefemi.domain.game.move.Move;
import nl.davefemi.domain.piece.PlayerColor;
import nl.davefemi.exception.BoardException;
import nl.davefemi.data.repository.GameRepository;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameService {
    private final GameSessionRepository gameSessionRepository;
    private final GameRepository gameRepository;
    private final MoveMapper moveMapper;
    private final GameResponseMapper gameResponseMapper;
    private final BoardMapper boardMapper;
    private final GameSessionMapper gameSessionMapper;

    public GameReponseDTO getNewGame(){
        Game game = gameRepository.saveGame(new Game());
//        gameSessionRepository.save(gameSessionMapper.mapDomainToEntity(game.getGameId().toString(), true, game.getTurn().getColor(), null));
        return gameResponseMapper.mapToDTO(game.getGameId(), null, null, "Game initialised");
    }

    private Game getGame(UUID gameId) throws FileNotFoundException {
        Game game = gameRepository.find(gameId);
        if (game == null)
            throw new FileNotFoundException("Game not found");
        return game;
    }

    private PlayerColor stringToPlayerColor(String color){
        for (PlayerColor c : PlayerColor.values()){
            if (c.getColor().equals(color.toLowerCase()))
                return c;
        }
        throw new BoardException("Color does not exist");
    }

    public BoardDTO getPositions(String gameId) throws FileNotFoundException {
        Game game =getGame(UUID.fromString(gameId));
        return boardMapper.mapDomainToDTO(game.getGameId(), game.getCopyOfBoard());
    }

    public GameReponseDTO getPlayerTurn(String gameId) throws FileNotFoundException {
        Game game = getGame(UUID.fromString(gameId));
        return gameResponseMapper.mapToDTO(game.getGameId(), game.getPlayerTurn(), null, null);
    }

    public GameReponseDTO isCheck(String gameId, String color) throws FileNotFoundException {
        Game game = getGame(UUID.fromString(gameId));
        return gameResponseMapper.mapToDTO(game.getGameId(), null, null, game.isCheck(stringToPlayerColor(color)));
    }

    public GameReponseDTO isCheckMate(String gameId, String color) throws FileNotFoundException {
        Game game = getGame(UUID.fromString(gameId));
        return gameResponseMapper.mapToDTO(game.getGameId(), null, null, game.isCheckMate(stringToPlayerColor(color)));
    }

    public GameReponseDTO getAvailableMoves(String gameId, String color) throws FileNotFoundException {
        Game game = getGame(UUID.fromString(gameId));
        List<Move> moves = game.getAvailableMoves(stringToPlayerColor(color));
        return gameResponseMapper.mapToDTO(game.getGameId(), null, null, moveMapper.mapDomainToDTO(moves));
    }

    public GameReponseDTO executeMove(String gameId, String color, MoveDTO move) throws FileNotFoundException {
        Game game = getGame(UUID.fromString(gameId));
        game.executeMove(stringToPlayerColor(color), moveMapper.mapDTOtoDomain(move));
        Optional<GameSession> session = gameSessionRepository.findById(gameId);
//        gameSessionRepository.save(gameSessionMapper.mapDomainToEntity(gameId, true, game.getTurn().getColor(), game.getLastMove()));
        return gameResponseMapper.mapToDTO(game.getGameId(), stringToPlayerColor(color), null, move);
    }

}
