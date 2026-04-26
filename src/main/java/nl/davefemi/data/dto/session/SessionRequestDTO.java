package nl.davefemi.data.dto.session;

import lombok.Data;
import nl.davefemi.data.dto.move.MoveRequestDTO;

@Data
public class SessionRequestDTO {
    private String sessionId;
    private String playerId;
    private MoveRequestDTO request;
}
