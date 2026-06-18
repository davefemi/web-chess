package nl.davefemi.chess.http.response.session;

import lombok.Data;

@Data
public class RequestedRematchResponse {
    private String newGameRequestedBy;
    private String newGameId;
}
