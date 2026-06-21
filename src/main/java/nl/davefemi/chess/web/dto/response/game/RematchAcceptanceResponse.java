package nl.davefemi.chess.web.dto.response.game;

import lombok.Data;

@Data
public class RematchAcceptanceResponse {
    private boolean accepted;
    private String by;
}
