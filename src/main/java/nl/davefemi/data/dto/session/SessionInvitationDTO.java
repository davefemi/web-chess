package nl.davefemi.data.dto.session;

import lombok.Data;

@Data
public class SessionInvitationDTO {
    private String sessionId;
    private String playerId;
    private String playerColor;
    private String inviteUrl;
}
