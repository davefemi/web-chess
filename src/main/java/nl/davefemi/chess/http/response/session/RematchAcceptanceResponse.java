package nl.davefemi.chess.http.response.session;

import lombok.Data;

@Data
public class RematchAcceptanceResponse {
    private boolean accepted;
    private String by;
    private String newGameId;
}
