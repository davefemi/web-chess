package nl.davefemi.webchess.session;

import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Token {
    @Getter
    private final String token;
    @Getter
    private final Instant expiresAt;
    private final Map<String, String> claims = new HashMap<>();

    public Token(String token, int timeToLive){
        this.token = token;
        this.expiresAt = Instant.now().plusSeconds(timeToLive);
    }

    public void setClaim(String key, String value){
        claims.put(key, value);
    }

    public String getClaim(String key){
        return claims.get(key);
    }

    public Map<String, String> getClaims(){
        return Map.copyOf(claims);
    }
}
