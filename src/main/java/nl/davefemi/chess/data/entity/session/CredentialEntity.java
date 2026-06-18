package nl.davefemi.chess.data.entity.session;

import lombok.Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
public class CredentialEntity {
    private String hashToken;
    private Instant expiresAt;
    private Map<String, String> claims = new HashMap<>();

    public String getClaim(String key){
        return claims.get(key);
    }

    public void expiresAtPlusSeconds(int secondsToAdd){
        expiresAt = Instant.now().plusSeconds(secondsToAdd);
    }
}
