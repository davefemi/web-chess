package nl.davefemi.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.data.entity.GameSessionEntity;
import nl.davefemi.data.entity.GameStateEntity;
import nl.davefemi.data.entity.PlayerEntity;
import nl.davefemi.exception.SessionException;
import nl.davefemi.game.board.PieceColor;
import nl.davefemi.game.Game;
import nl.davefemi.exception.BoardException;
import nl.davefemi.session.GameSession;
import nl.davefemi.session.Player;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GameSessionMapper {
    private final GameStateMapper gameStateMapper;

    public GameSessionEntity mapDomainToEntity(GameSession session){
        GameSessionEntity sessionEntity = new GameSessionEntity();
        for (Game g: session.getGames()){
            sessionEntity.getGames().add(gameStateMapper.mapDomainToEntity(g));
        }
        sessionEntity.setSessionId(session.getSessionId().toString());
        sessionEntity.setActive(session.isActive());
        sessionEntity.setPlayers(mapPlayersToEntities(session.getPlayers()));
        return sessionEntity;
    }

    private List<PlayerEntity> mapPlayersToEntities(List<Player> players){
        List<PlayerEntity> entities = new ArrayList<>();
        for (Player player : players){
            PlayerEntity entity = new PlayerEntity();
            entity.setId(player.getId().toString());
            entity.setPlayerColor(player.getPlayerColor().getColor());
            entities.add(entity);
        }
        return entities;
    }

    public GameSession mapEntityToDomain(GameSessionEntity entity) throws BoardException, SessionException {
        List<Player> players = new ArrayList<>();
        for (PlayerEntity player : entity.getPlayers()){
            players.add(new Player(UUID.fromString(player.getId()), PieceColor.fromString(player.getPlayerColor())));
        }
        List<Game> games = new ArrayList<>();
        for (GameStateEntity g: entity.getGames()){
            games.add(gameStateMapper.mapEntityToDomain(g));
        }
        return new GameSession(UUID.fromString(entity.getSessionId()), entity.isActive(), games, players);
    }
}
