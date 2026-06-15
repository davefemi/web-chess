package nl.davefemi.webchess.data.entity.session;

import lombok.Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
public class TokenEntity {
    String hash;
    private Instant expiresAt;
    Map<String, String> claims = new HashMap<>();

}
