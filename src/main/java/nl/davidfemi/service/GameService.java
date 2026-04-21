package nl.davidfemi.service;

import lombok.RequiredArgsConstructor;
import nl.davidfemi.data.dto.*;
import nl.davidfemi.data.mapper.*;
import nl.davidfemi.domain.game.Game;
import nl.davidfemi.domain.game.utility.Move;
import nl.davidfemi.domain.pieces.PieceType;
import nl.davidfemi.domain.pieces.PlayerColor;
import nl.davidfemi.exception.BoardException;
import nl.davidfemi.data.repository.GameRepository;
import org.springframework.stereotype.Service;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameRepository gameRepository;
    private final MoveMapper moveMapper;
    private final CastlingMoveMapper castlingMoveMapper;
    private final PositionMapper positionMapper;
    private final GameResponseMapper gameResponseMapper;
    private final BoardMapper boardMapper;

    public GameReponseDTO getNewGame(){
        Game game = gameRepository.saveGame(new Game());
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

    private PieceType stringToPieceType(String type){
        for (PieceType t : PieceType.values()){
            if (t.getLabel().equals(type.toLowerCase()))
                return t;
        }
        throw new BoardException("Piece type does not exist");
    }

    public BoardDTO getPositions(String gameId) throws FileNotFoundException {
        Game game =getGame(UUID.fromString(gameId));
        return boardMapper.mapDomainToDTO(game.getGameId(), game.getCopyOfBoard());
    }

    public GameReponseDTO getPlayerTurn(String gameId) throws FileNotFoundException {
        Game game = getGame(UUID.fromString(gameId));
        return gameResponseMapper.mapToDTO(game.getGameId(), game.getTurn(), null, null);
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
        return gameResponseMapper.mapToDTO(game.getGameId(), stringToPlayerColor(color), null, move);
    }

    public GameReponseDTO executeCastling(String gameId, String color, CastlingMoveDTO move) throws FileNotFoundException {
        Game game = getGame(UUID.fromString(gameId));
        game.executeCastling(stringToPlayerColor(color), castlingMoveMapper.mapDTOtoDomain(move));
        return gameResponseMapper.mapToDTO(game.getGameId(), stringToPlayerColor(color), null, move);
    }

    public GameReponseDTO promotePawn(String gameId, String color, PositionDTO position, String pieceType) throws FileNotFoundException {
        Game game = getGame(UUID.fromString(gameId));
        boolean res = game.promotePawn(stringToPlayerColor(color),
                positionMapper.mapDTOtoDomain(position),
                stringToPieceType(pieceType));
        return gameResponseMapper.mapToDTO(game.getGameId(), stringToPlayerColor(color), stringToPieceType(pieceType), res);
    }
}
