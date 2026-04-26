package nl.davefemi.session;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.time.Instant;

@Getter
public class AccessCode {
    private final String token;
    private final String sessionId;
    private final Instant expiresAt;

    AccessCode(String token, String sessionId, Instant expiresAt){
        this.token = token;
        this.sessionId = sessionId;
        this.expiresAt = expiresAt;
    }
}
