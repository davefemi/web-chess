package nl.davefemi.chess.data.dto.session;

import lombok.Data;

import java.util.Optional;

@Data
public class SessionRequestDTO {
    private String playerId;
    private Optional<Object> request;
}
