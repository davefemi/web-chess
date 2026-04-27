package nl.davefemi.webchess.data.dto.session;

import lombok.Data;

@Data
public class SessionResponseDTO {
    private String sessionId;
    private String playerId;
    private String color;
    private Object message;
}
