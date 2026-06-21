package nl.davefemi.chess.data.repository;

import lombok.RequiredArgsConstructor;
import nl.davefemi.chess.data.entity.session.CredentialEntity;
import nl.davefemi.chess.exception.UnauthorizedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Repository
public class CredentialStore implements CredentialRepository {
    private final RedisTemplate<String, CredentialEntity> redisTemplate;

    @Override
    public void saveCredential(String tokenType, CredentialEntity credential) {
        redisTemplate.opsForValue().set(tokenType +": " + credential.getHashToken(), credential, Duration.between(Instant.now(), credential.getExpiresAt()));
    }

    @Override
    public CredentialEntity retrieveCredential(String tokenType, String tokenHash, boolean deleteOnRetrieval) {
        CredentialEntity token = redisTemplate.opsForValue().get(tokenType + ": " + tokenHash);
        if (token == null) {
            throw new UnauthorizedException("Credential not found");
        }
        if (deleteOnRetrieval) {
            deleteCredential(tokenType, tokenHash);
        }
        return token;
    }

    private void deleteCredential(String tokenType, String tokenHash){
        redisTemplate.delete(tokenType + ": " + tokenHash);
    }
}
