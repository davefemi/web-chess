package nl.davefemi.chess.http.response.session;

import lombok.Data;

@Data
public class EndedSessionResponse {
    private String sessionStatus = "ended";
    private String endedBy;
}
