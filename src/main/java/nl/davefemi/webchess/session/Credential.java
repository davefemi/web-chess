package nl.davefemi.webchess.session;

import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;

public class Credential {
    @Getter
    private final String token;
    @Getter
    private final String hashToken;
    @Getter
    private final Instant expiresAt;
    private final Map<String, String> claims = new HashMap<>();

    public Credential(String token, int timeToLive) throws NoSuchAlgorithmException {
        this.token = token;
        this.expiresAt = Instant.now().plusSeconds(timeToLive);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
        this.hashToken = HexFormat.of().formatHex(hash);
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
