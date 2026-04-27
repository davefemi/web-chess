package nl.davefemi.webchess.data.entity;

import lombok.Data;

import java.time.Instant;

@Data
public class AccessCodeEntity {
    private String token;
    private String sessionId;
    private Instant expiresAt;
}
