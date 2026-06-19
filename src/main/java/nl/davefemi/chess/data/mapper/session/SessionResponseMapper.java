package nl.davefemi.chess.data.mapper.session;

import nl.davefemi.chess.http.response.game.RematchAcceptanceResponse;
import nl.davefemi.chess.http.response.game.RequestedRematchResponse;
import nl.davefemi.chess.http.response.game.RequestedSessionResponse;
import nl.davefemi.chess.http.response.session.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SessionResponseMapper {
    @Value ("${external.websocket_id}")
    private String websocketId;


    public SessionResponse getSessionResponse(String color, Object message){
        SessionResponse dto = new SessionResponse();
        dto.setColor(color);
        dto.setMessage(message);
        return dto;
    }

    public RequestedSessionResponse getRequestedSessionResponse(String messageId, String playerToken, String playerColor, String accessToken){
        RequestedSessionResponse dto = new RequestedSessionResponse();
        dto.setPlayerToken(playerToken);
        dto.setPlayerColor(playerColor);
        dto.setJoinToken(accessToken);
        dto.setWebsocketId(websocketId);
        dto.setPlayerId(messageId);
        return dto;
    }

    public AcceptedSessionResponse getAcceptedSessionResponse(String gameId, String messageId, String playerToken, String playerColor){
        AcceptedSessionResponse dto = new AcceptedSessionResponse();
        dto.setPlayerToken(playerToken);
        dto.setPlayerColor(playerColor);
        dto.setWebsocketId(websocketId);
        dto.setGameId(gameId);
        dto.setPlayerId(messageId);
        return dto;
    }

    public EndedSessionResponse getEndedSessionResponse(String playerColor){
        EndedSessionResponse dto = new EndedSessionResponse();
        dto.setEndedBy(playerColor);
        return dto;
    }

    public RequestedRematchResponse getRequestedRematchResponse(String playerColor){
        RequestedRematchResponse dto = new RequestedRematchResponse();
        dto.setNewGameRequestedBy(playerColor);
        return dto;
    }

    public RematchAcceptanceResponse getRematchAcceptanceResponse(boolean accepted, String playerColor){
        RematchAcceptanceResponse dto = new RematchAcceptanceResponse();
        dto.setAccepted(accepted);
        dto.setBy(playerColor);
        return dto;
    }
}
