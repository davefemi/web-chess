package nl.davefemi.chess.web.dto.response.session;

import lombok.Data;

@Data
public class AcceptedSessionResponse {
    private String playerToken;
    private String playerColor;
    private String websocketId;
    private String channelId;
}
