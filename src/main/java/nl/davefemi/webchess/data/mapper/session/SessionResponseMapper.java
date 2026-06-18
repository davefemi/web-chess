package nl.davefemi.webchess.data.mapper.session;

import nl.davefemi.webchess.data.dto.session.JoinSessionDTO;
import nl.davefemi.webchess.data.dto.session.SessionInitiationDTO;
import nl.davefemi.webchess.data.dto.session.SessionResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SessionResponseMapper {
    @Value ("${external.websocket_id}")
    private String websocketId;


    public SessionResponseDTO mapToDTO(String color, Object message){
        SessionResponseDTO dto = new SessionResponseDTO();
        dto.setColor(color);
        dto.setMessage(message);
        return dto;
    }

    public SessionInitiationDTO mapToInitiationResponseDTO(String subscriptionId, String messageId, String playerToken, String playerColor, String accessToken){
        SessionInitiationDTO dto = new SessionInitiationDTO();
        dto.setPlayerToken(playerToken);
        dto.setPlayerColor(playerColor);
        dto.setJoinToken(accessToken);
        dto.setWebsocketId(websocketId);
        dto.setGameId(subscriptionId);
        dto.setPlayerId(messageId);
        return dto;
    }

    public JoinSessionDTO mapToJoinSessionResponseDTO(String subscriptionId, String messageId, String playerToken, String playerColor){
        JoinSessionDTO dto = new JoinSessionDTO();
        dto.setPlayerToken(playerToken);
        dto.setPlayerColor(playerColor);
        dto.setWebsocketId(websocketId);
        dto.setGameId(subscriptionId);
        dto.setPlayerId(messageId);
        return dto;
    }

}
