package nl.davefemi.webchess.data.mapper.session;

import nl.davefemi.webchess.data.entity.session.CredentialEntity;
import nl.davefemi.webchess.session.Credential;
import org.springframework.stereotype.Component;

@Component
public class CredentialMapper {

    public CredentialEntity mapToEntity(Credential credential) {
        CredentialEntity entity = new CredentialEntity();
        entity.setHashToken(credential.getHashToken());
        entity.setExpiresAt(credential.getExpiresAt());
        credential.getClaims().forEach(entity.getClaims()::put);
        return entity;
    }
}
