package nl.davefemi.webchess.data.dto.session;

import lombok.Data;

@Data
public class SessionResponseDTO {
    private String playerToken;
    private String color;
    private Object message;
}
