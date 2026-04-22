package nl.davefemi.data.mapper;

import nl.davefemi.data.entity.GameSession;
import nl.davefemi.domain.game.move.MoveRecord;
import org.springframework.stereotype.Component;

@Component
public class GameSessionMapper {

    public GameSession mapDomainToEntity(String gameId, boolean status, String turn, MoveRecord moveRecord){
        GameSession session = new GameSession();
        session.setGameId(gameId);
        session.setStatus(status);
        session.setTurn(turn);
        if (moveRecord != null)
            session.getMoves().add(moveRecord);
        return session;
    }

}
