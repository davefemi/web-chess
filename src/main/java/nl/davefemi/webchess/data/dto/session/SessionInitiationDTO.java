package nl.davefemi.webchess.data.dto.session;

import lombok.Data;

@Data
public class SessionInitiationDTO {
    private String playerToken;
    private String playerColor;
    private String inviteUrl;
}
