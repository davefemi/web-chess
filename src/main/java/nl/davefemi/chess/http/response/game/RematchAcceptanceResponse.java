package nl.davefemi.chess.http.response.game;

import lombok.Data;

@Data
public class RematchAcceptanceResponse {
    private boolean accepted;
    private String by;
}
