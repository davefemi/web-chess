package nl.davefemi.webchess.data.mapper.session;

import nl.davefemi.webchess.data.entity.session.TokenEntity;
import nl.davefemi.webchess.session.Token;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Component
public class TokenMapper {

    public TokenEntity mapToEntity(Token token) throws NoSuchAlgorithmException {
        TokenEntity entity = new TokenEntity();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(token.getToken().getBytes(StandardCharsets.UTF_8));
        entity.setHash(HexFormat.of().formatHex(hash));
        entity.setExpiresAt(token.getExpiresAt());
        token.getClaims().forEach(entity.getClaims()::put);
        return entity;
    }

    public String mapTokenToTokenHash(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }
}
