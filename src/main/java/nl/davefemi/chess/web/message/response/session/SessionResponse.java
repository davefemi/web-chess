package nl.davefemi.chess.web.message.response.session;

import lombok.Data;

@Data
public class SessionResponse {
    private String color;
    private Object message;
}
