package nl.davefemi.webchess.data.entity.session;

import lombok.Data;

@Data
public class PlayerEntity {
    private String id;
    private String playerColor;
    private String sessionId;
}
