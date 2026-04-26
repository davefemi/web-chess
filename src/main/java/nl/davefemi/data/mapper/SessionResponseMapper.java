package nl.davefemi.data.mapper;

import nl.davefemi.data.dto.session.SessionInvitationDTO;
import nl.davefemi.data.dto.session.SessionResponseDTO;
import nl.davefemi.session.GameSession;
import nl.davefemi.session.Player;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;

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

    public SessionInvitationDTO mapInvitationToDTO(GameSession session, Player player, String inviteUrl){
        SessionInvitationDTO dto = new SessionInvitationDTO();
        dto.setSessionId(session.getSessionId().toString());
        dto.setPlayerId(player.getId().toString());
        dto.setPlayerColor(player.getPlayerColor().getColor());
        dto.setInviteUrl(inviteUrl);
        return dto;
    }
}
