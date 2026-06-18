package nl.davefemi.chess.data.entity.session;

import lombok.Data;

@Data
public class PlayerEntity {
    private String id;
    private String messageId;
    private String sessionId;
    private String playerColor;
}
