package nl.davefemi.webchess.data.mapper;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.entity.GameSessionEntity;
import nl.davefemi.webchess.data.entity.GameStateEntity;
import nl.davefemi.webchess.data.entity.PlayerEntity;
import nl.davefemi.webchess.exception.SessionException;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.exception.BoardException;
import nl.davefemi.webchess.session.GameSession;
import nl.davefemi.webchess.session.Player;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GameSessionMapper {
    private final GameStateMapper gameStateMapper;


    public GameSessionEntity mapDomainToEntity(GameSession session) throws BoardException {
        GameSessionEntity sessionEntity = new GameSessionEntity();
        for (Game g: session.getGames()){
            sessionEntity.getGames().add(gameStateMapper.mapDomainToEntity(g));
        }
        sessionEntity.setSessionId(session.getSessionId().toString());
        sessionEntity.setActiveSession(session.isActive());
        sessionEntity.setPlayers(mapPlayersToEntities(session.getPlayers()));
        if (session.getPlayerToAccept() != null)
            sessionEntity.setPlayerToAccept(mapPlayerToEntity(session.getPlayerToAccept()));
        return sessionEntity;
    }

    private List<PlayerEntity> mapPlayersToEntities(List<Player> players){
        return players.stream().map(this::mapPlayerToEntity).collect(Collectors.toList());
    }

    private PlayerEntity mapPlayerToEntity(Player player){
        PlayerEntity entity = new PlayerEntity();
        entity.setId(player.getId().toString());
        entity.setPlayerColor(player.getPlayingColor().getColor());
        return entity;
    }

    private Player mapEntityToPlayer(PlayerEntity player){
        return new Player(UUID.fromString(player.getId()), PieceColor.fromString(player.getPlayerColor()));
    }

    public GameSession mapEntityToDomain(GameSessionEntity entity) throws BoardException, SessionException {
        List<Player> players = new ArrayList<>();
        for (PlayerEntity player : entity.getPlayers()){
            players.add(mapEntityToPlayer(player));
        }
        List<Game> games = new ArrayList<>();
        for (GameStateEntity g: entity.getGames()){
            games.add(gameStateMapper.mapEntityToDomain(g));
        }
        return new GameSession(UUID.fromString(entity.getSessionId()),
                entity.isActiveSession(), games, players,
                entity.getPlayerToAccept() == null ? null : mapEntityToPlayer(entity.getPlayerToAccept()));
    }
}
