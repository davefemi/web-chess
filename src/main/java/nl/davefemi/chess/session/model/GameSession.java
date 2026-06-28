package nl.davefemi.chess.session.model;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.exception.GameException;
import nl.davefemi.chess.exception.SessionException;
import nl.davefemi.chess.gameplay.model.game.Color;
import nl.davefemi.chess.gameplay.model.game.Game;
import nl.davefemi.chess.session.service.TokenGenerator;

import java.util.*;

@Slf4j
public final class GameSession {
    @Getter
    private final UUID sessionId;
    @Getter
    private boolean isActive = true;
    private final List<Game> games = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    @Getter
    private Player playerToAccept;

    public GameSession() {
        this.sessionId = UUID.randomUUID();
        Game game = new Game(TokenGenerator.generateToken(8));
        games.add(game);
    }

    public Game getRematch(Player player) throws SessionException {
        if (games.getLast().getStatus().isWaiting()) {
            throw new SessionException("A new game has already been initiated");
        }
        if (games.getLast().getStatus().isActive()){
            throw new SessionException("End running game first");
        }
        if (players.contains(player)) {
            Game game = new Game(TokenGenerator.generateToken(8));
            games.add(game);
            for (Player p : players) {
                if (!p.equals(player)) {
                    playerToAccept = p;
                }
            }
            return game;
        }
        throw new SessionException("Player is not part of this session");
    }

    public void acceptRematch(Player player) throws GameException, SessionException {
        if (!player.equals(playerToAccept)) {
            throw new SessionException("Invalid player");
        }
        if(!games.getLast().getStatus().isWaiting()) {
            throw new GameException("Game is not eligible for a rematch");
        }
        games.getLast().start();
        playerToAccept = null;
    }

    public void startSession() throws GameException {
        games.getLast().start();
    }


    public void endSession(){
        isActive = false;
    }

    public GameSession(UUID sessionId, boolean active, List<Game> games, List<Player> players, Player playerToAccept)
            throws SessionException {
        this.sessionId = sessionId;
        this.isActive = active;
        this.games.addAll(games);
        for (Player p : players){
            if (checkExistingPlayers(p)) {
                this.players.add(p);
            }
            if (p.equals(playerToAccept)) {
                this.playerToAccept = playerToAccept;
            }
        }

    }

    public synchronized Player addPlayer(Color color) throws SessionException {
        Player player = new Player(UUID.randomUUID(), TokenGenerator.generateToken(8), sessionId, color);
        if (checkExistingPlayers(player)) {
            players.add(player);
        }
        return player;
    }

    public synchronized Player addPlayer() throws SessionException {
        Player player;
        if (players.size() > 1 ) {
            throw new SessionException("Amount of players possible exceeded");
        }
        if (players.size() == 1){
            Color color = Color.getOpponent(players.getLast().getColor());
            player = new Player(UUID.randomUUID(), TokenGenerator.generateToken(8), sessionId, color);
            players.add(player);
            return player;
        }
        player = new Player(UUID.randomUUID(), TokenGenerator.generateToken(8), sessionId, Color.WHITE);
        players.add(player);
        return player;
    }

    public Game getCurrentGame() throws SessionException {
        if (isActive) {
            return games.getLast();
        }
        throw new SessionException("Session is not active");
    }

    public List<Game> getGames() throws SessionException {
        if (isActive) {
            return new ArrayList<>(games);
        }
        throw new SessionException("Session is not active");
    }


    public List<Player> getPlayers(){
        return new ArrayList<>(players);
    }

    public Player getOpponent(Player player) throws SessionException {
        if (!players.contains(player)) {
            throw new SessionException("Player is not part of this session");
        }
        for (Player p : players){
            if (!p.equals(player))
                return p;
        }
        return null;
    }

    private boolean checkExistingPlayers(Player player) throws SessionException {
        if (players.size() > 1 ) {
            throw new SessionException("Amount of players possible exceeded");
        }
        for (Player p : players) {
            if (p.getColor() == player.getColor()) {
                throw new SessionException(player.getColor() + " is already taken");
            }
            if (p.getId() == player.getId()) {
                throw new SessionException("Player already joined");
            }
        }
        return true;
    }
}
