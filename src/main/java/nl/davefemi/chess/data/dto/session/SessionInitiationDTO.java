package nl.davefemi.chess.data.dto.session;

import lombok.Data;

@Data
public class SessionInitiationDTO {
    private String playerToken;
    private String playerColor;
    private String joinToken;
    private String websocketId;
    private String gameId;
    private String playerId;
}
