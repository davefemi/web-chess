package nl.davefemi.chess.http.response.session;

import lombok.Data;

@Data
public class RequestedSessionResponse {
    private String playerToken;
    private String playerColor;
    private String joinToken;
    private String websocketId;
    private String gameId;
    private String playerId;
}
