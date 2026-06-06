package nl.davefemi.webchess.data.mapper;

import nl.davefemi.webchess.data.dto.session.PlayerDTO;
import nl.davefemi.webchess.game.board.PieceColor;
import nl.davefemi.webchess.session.Player;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class PlayerMapper {

    public Player mapDTOtoDomain(PlayerDTO player){
        return new Player(UUID.fromString(player.getPlayerId()), PieceColor.fromString(player.getPlayingColor()));
    }
}
