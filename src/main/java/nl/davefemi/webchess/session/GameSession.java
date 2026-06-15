package nl.davefemi.webchess.session;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.exception.GameException;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.game.Game;
import nl.davefemi.webchess.exception.SessionException;

import java.util.*;

@Slf4j
public final class GameSession {
    @Getter
    private final UUID sessionId;
    @Getter @Setter
    private boolean isActive = true;
    private final List<Game> games = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    @Getter
    private Player playerToAccept;

    public GameSession() {
        this.sessionId = UUID.randomUUID();
        Game game = new Game();
        games.add(game);
    }

    public Game getRematch(Player player) throws SessionException {
        if (games.getLast().getStatus().isWaiting())
            throw new SessionException("A new game has already been initiated");
        if (games.getLast().getStatus().isActive()){
            throw new SessionException("End running game first");
        }
        if (players.contains(player)) {
            Game game = new Game();
            games.add(game);
            for (Player p : players) {
                if (!p.equals(player))
                    playerToAccept = p;
            }
            return game;
        }
        throw new SessionException("Player is not part of this session");
    }

    public void acceptRematch(Player player) throws GameException, SessionException {
        if (!player.equals(playerToAccept))
            throw new SessionException("Invalid player");
        games.getLast().start();
        playerToAccept = null;
    }

    public void startSession() throws GameException {
        games.getLast().start();
    }

    public boolean endSession(Player player){
        games.getLast().surrender(player.getColor());
        return true;
    }

    public GameSession(UUID sessionId, boolean active, List<Game> games, List<Player> players, Player playerToAccept)
            throws SessionException {
        this.sessionId = sessionId;
        this.games.addAll(games);
        for (Player p : players){
            if (checkExistingPlayers(p))
                this.players.add(p);
            if (p.equals(playerToAccept))
                this.playerToAccept = playerToAccept;
        }
    }

    public Player addPlayer(Color color) throws SessionException {
        Player player = new Player(UUID.randomUUID(), sessionId, color);
        if (checkExistingPlayers(player))
            players.add(player);
        return player;
    }

    public Player addPlayer() throws SessionException {
        Player player;
        if (players.size() > 1 )
            throw new SessionException("Amount of players possible exceeded");
        if (players.size() == 1){
            Color color = Color.getOpponent(players.getLast().getColor());
            player = new Player(UUID.randomUUID(), sessionId, color);
            players.add(player);
            return player;
        }
        player = new Player(UUID.randomUUID(), sessionId, Color.WHITE);
        players.add(player);
        return player;
    }

    public Game getCurrentGame() {
        return games.getLast();
    }

    public List<Game> getGames() {
        return new ArrayList<>(games);
    }

    public Player getPlayer(UUID id){
        for (Player p: players){
            if (p.getId().equals(id))
                return p;
        }
        return null;
    }

    public List<Player> getPlayers(){
        return new ArrayList<>(players);
    }

    private boolean checkExistingPlayers(Player player) throws SessionException {
        if (players.size() > 1 )
            throw new SessionException("Amount of players possible exceeded");
        for (Player p : players) {
            if (p.getColor() == player.getColor())
                throw new SessionException(player.getColor() + " is already taken");
            if (p.getId() == player.getId())
                throw new SessionException("Player already joined");
        }
        return true;
    }
}
