package nl.davefemi.chess.data.mapper.session;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.data.entity.session.GameSessionEntity;
import nl.davefemi.chess.data.entity.session.GameStateEntity;
import nl.davefemi.chess.data.entity.session.PlayerEntity;
import nl.davefemi.chess.exception.SessionNotFoundException;
import nl.davefemi.chess.play.model.game.Color;
import nl.davefemi.chess.play.model.game.Game;
import nl.davefemi.chess.exception.BoardException;
import nl.davefemi.chess.session.model.GameSession;
import nl.davefemi.chess.session.model.Player;
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
        entity.setMessageId(player.getMessageId());
        entity.setSessionId(player.getSessionId().toString());
        entity.setPlayerColor(player.getColor().toString());
        return entity;
    }

    private Player mapEntityToPlayer(PlayerEntity player, String sessionId){
        return new Player(UUID.fromString(player.getId()),
                player.getMessageId(),
                UUID.fromString(sessionId),
                Color.fromString(player.getPlayerColor()));
    }

    public GameSession mapEntityToDomain(GameSessionEntity entity) throws BoardException, SessionNotFoundException {
        List<Player> players = new ArrayList<>();
        for (PlayerEntity player : entity.getPlayers()){
            players.add(mapEntityToPlayer(player, entity.getSessionId()));
        }
        List<Game> games = new ArrayList<>();
        for (GameStateEntity g: entity.getGames()){
            games.add(gameStateMapper.mapEntityToDomain(g));
        }
        return new GameSession(UUID.fromString(entity.getSessionId()),
                entity.isActiveSession(), games, players,
                entity.getPlayerToAccept() == null ? null : mapEntityToPlayer(entity.getPlayerToAccept(), entity.getSessionId()));
    }
}
