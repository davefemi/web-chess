package nl.davefemi.data.mapper;

import nl.davefemi.data.dto.SessionResponseDTO;
import nl.davefemi.session.GameSession;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class SessionResponseMapper {

    public SessionResponseDTO mapToDTO(GameSession session, @Nullable String color, Object message){
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setSessionId(session.getSessionId().toString());
        if (!session.getPlayers().isEmpty())
            dto.setPlayerId(session.getPlayers().getLast().getId().toString());
        dto.setColor(color);
        dto.setMessage(message);
        return dto;
    }

    public SessionResponseDTO mapToDTO(String sessionId, @Nullable String color, Object message){
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setSessionId(sessionId);
        dto.setColor(color);
        dto.setMessage(message);
        return dto;
    }
}
