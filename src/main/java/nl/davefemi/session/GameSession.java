package nl.davefemi.session;

import nl.davefemi.game.board.PieceColor;
import nl.davefemi.game.Game;
import nl.davefemi.exception.SessionException;

import java.util.*;

public class GameSession {
    private final UUID sessionId;
    private boolean isActive = true;
    private final List<Game> games = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();

    public GameSession(){
        this.sessionId = UUID.randomUUID();
        Game game = new Game();
        games.add(game);
    }

    public Game startNewGame() throws SessionException {
        if (games.getLast().isGameActive()){
            throw new SessionException("End running game first");
        }
        Game game = new Game();
        games.add(game);
        return game;
    }

    public void startSession(){
        games.getLast().start();
    }

    public boolean endCurrentGame(){
        games.getLast().setInactive();
        return true;
    }

    public GameSession(UUID sessionId, boolean active, List<Game> games, List<Player> players) throws SessionException {
        this.sessionId = sessionId;
        this.games.addAll(games);
        for (Player p : players){
            if (checkExistingPlayers(p))
                this.players.add(p);
        }
    }

    public void setInactive() {
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public Player createPlayer(PieceColor color) throws SessionException {
        Player player = new Player(UUID.randomUUID(), color);
        if (checkExistingPlayers(player))
            players.add(player);
        return player;
    }

    public Player createPlayer() throws SessionException {
        Player player;
        if (players.size() > 1 )
            throw new SessionException("Amount of players possible exceeded");
        if (players.size() == 1){
            PieceColor color = PieceColor.getOpponent(players.getLast().getPlayerColor());
            player = new Player(UUID.randomUUID(), color);
            players.add(player);
            return player;
        }
        player = new Player(UUID.randomUUID(), PieceColor.WHITE);
        players.add(player);
        return player;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public Game getCurrentGame() {
        return games.getLast();
    }

    public List<Game> getGames() {
        return new ArrayList<>(games);
    }

    public List<Player> getPlayers(){
        return new ArrayList<>(players);
    }

    private boolean checkExistingPlayers(Player player) throws SessionException {
        if (players.size() > 1 )
            throw new SessionException("Amount of players possible exceeded");
        for (Player p : players) {
            if (p.getPlayerColor() == player.getPlayerColor())
                throw new SessionException(player.getPlayerColor() + " is already taken");
            if (p.getId() == player.getId())
                throw new SessionException("Player already joined");
        }
        return true;
    }
}
