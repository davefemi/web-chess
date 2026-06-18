package nl.davefemi.webchess.data.dto.session;

import lombok.Data;

@Data
public class JoinSessionDTO {
    private String playerToken;
    private String playerColor;
    private String websocketId;
    private String gameId;
    private String playerId;
}
