package nl.davefemi.chess.play.model.game;

import lombok.Getter;

import java.util.Optional;

public record GameStatus(GamePhase phase, Optional<Color> winner, Optional<GameEndReason> reason) {
    public enum GamePhase {
        WAITING_FOR_PLAYERS("waiting"),
        ACTIVE("active"),
        ENDED("ended");

        @Getter
        private final String phase;

        GamePhase(String phase){
            this.phase = phase;
        }
    }

    public enum GameEndReason{
        CHECKMATE("checkmate"),
        STALEMATE("stalemate"),
        SURRENDER("surrender");

        @Getter
        private final String reason;

        GameEndReason(String reason){
            this.reason = reason;
        }

    }
    public static GameStatus waiting(){
        return new GameStatus(GamePhase.WAITING_FOR_PLAYERS, Optional.empty(), Optional.empty());
    }

    public static GameStatus active(){
        return new GameStatus(GamePhase.ACTIVE, Optional.empty(), Optional.empty());
    }

    public static GameStatus checkmate(Color winner){
        return new GameStatus(GamePhase.ENDED, Optional.of(winner), Optional.of(GameEndReason.CHECKMATE));
    }

    public static GameStatus surrender(Color color){
        return new GameStatus(GamePhase.ENDED, Optional.of(Color.getOpponent(color)), Optional.of(GameEndReason.SURRENDER));
    }

    public static GameStatus stalemate(){
        return new GameStatus(GamePhase.ENDED, Optional.empty(), Optional.of(GameEndReason.STALEMATE));
    }

    public boolean isActive(){
        return phase == GamePhase.ACTIVE;
    }

    public boolean isFinished(){
        return phase == GamePhase.ENDED;
    }

    public boolean isWaiting(){
        return phase == GamePhase.WAITING_FOR_PLAYERS;
    }
}
