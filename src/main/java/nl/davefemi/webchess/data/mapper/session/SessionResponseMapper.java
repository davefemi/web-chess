package nl.davefemi.webchess.data.mapper.session;

import nl.davefemi.webchess.data.dto.session.SessionInitiationDTO;
import nl.davefemi.webchess.data.dto.session.SessionResponseDTO;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class SessionResponseMapper {

    public SessionResponseDTO mapToDTO( @Nullable String color, Object message){
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setColor(color);
        dto.setMessage(message);
        return dto;
    }

    public SessionInitiationDTO mapInvitationToDTO(String playerToken, String playerColor, String inviteUrl){
        SessionInitiationDTO dto = new SessionInitiationDTO();
        dto.setPlayerToken(playerToken);
        dto.setPlayerColor(playerColor);
        dto.setInviteUrl(inviteUrl);
        return dto;
    }

}
