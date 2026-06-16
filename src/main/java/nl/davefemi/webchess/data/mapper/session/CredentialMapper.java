package nl.davefemi.webchess.data.mapper.session;

import nl.davefemi.webchess.data.entity.session.CredentialEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;

@Component
public class CredentialMapper {

    public CredentialEntity mapToEntity(String hashToken, Instant expiresAt, Map<String, String> claims) {
        CredentialEntity entity = new CredentialEntity();
        entity.setHashToken(hashToken);
        entity.setExpiresAt(expiresAt);
        entity.getClaims().putAll(claims);
        return entity;
    }
}
