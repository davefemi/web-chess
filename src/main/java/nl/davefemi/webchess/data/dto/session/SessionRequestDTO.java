package nl.davefemi.webchess.data.dto.session;

import lombok.Data;
import nl.davefemi.webchess.data.dto.move.MoveRequestDTO;

@Data
public class SessionRequestDTO {
    private String sessionId;
    private String playerId;
    private MoveRequestDTO request;
}
