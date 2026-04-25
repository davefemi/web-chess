package nl.davefemi.data.dto;

import lombok.Data;

@Data
public class SessionResponseDTO {
    private String sessionId;
    private String playerId;
    private String color;
    private Object message;
}
