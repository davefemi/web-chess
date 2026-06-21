package nl.davefemi.chess.web.message.response.session;

import lombok.Data;

@Data
public class RequestedSessionResponse {
    private String playerToken;
    private String playerColor;
    private String joinToken;
    private String websocketId;
    private String channelId;
}
