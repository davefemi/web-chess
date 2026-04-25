package nl.davefemi.session;

import nl.davefemi.game.board.PieceColor;
import nl.davefemi.game.Game;
import nl.davefemi.exception.SessionException;

import java.util.*;

public class GameSession {
    private final UUID sessionId;
    private final List<Game> games = new ArrayList<>();
    private final Map<PieceColor, Player> players = new HashMap<>();

    public GameSession(){
        this.sessionId = UUID.randomUUID();
        startNewGame();
    }

    public Game startNewGame(){
        Game game = new Game();
        games.add(game);
        return game;
    }

    public GameSession(UUID sessionId, List<Game> games, List<Player> players) throws SessionException {
        this.sessionId = sessionId;
        this.games.addAll(games);
        for (Player p : players){
            if (checkExistingPlayers(p))
                this.players.put(p.getPlayerColor(),p);
        }
    }

    public Player createPlayer(PieceColor color) throws SessionException {
        Player player = new Player(UUID.randomUUID(), color);
        if (checkExistingPlayers(player))
            players.put(color, player);
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
        return new ArrayList<>(players.values());
    }

    private boolean checkExistingPlayers(Player player) throws SessionException {
        if (players.size() > 1 )
            throw new SessionException("Amount of players possible exceeded");
        if (players.containsKey(player.getPlayerColor()))
            throw new SessionException(player.getPlayerColor() + " is already taken");
        for (Map.Entry<PieceColor, Player> entry : players.entrySet()) {
            Player p = entry.getValue();
            if (p.getId() == player.getId())
                throw new SessionException("Player already joined");
        }
        return true;
    }
}
