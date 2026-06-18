package nl.davefemi.chess.http.response.session;

import lombok.Data;

@Data
public class SessionResponse {
    private String color;
    private Object message;
}
