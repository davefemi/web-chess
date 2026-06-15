package nl.davefemi.webchess.data.repository;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.entity.session.CredentialEntity;
import nl.davefemi.webchess.exception.UnauthorizedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Repository
public class CredentialStore implements CredentialRepository {
    private final RedisTemplate<String, CredentialEntity> redisTemplate;

    @Override
    public void saveCredentials(String tokenType, CredentialEntity token) {
        redisTemplate.opsForValue().set(tokenType +": " + token.getHashToken(), token, Duration.between(Instant.now(), token.getExpiresAt()));
    }

    @Override
    public CredentialEntity retrieveCredentials(String tokenType, String tokenHash, boolean deleteOnRetrieval) {
        CredentialEntity token = redisTemplate.opsForValue().get(tokenType + ": " + tokenHash);
        if (token == null)
            throw new UnauthorizedException("Token not found");
        if (deleteOnRetrieval)
            deleteCredentials(tokenType, tokenHash);
        return token;
    }

    private void deleteCredentials(String tokenType, String tokenHash){
        redisTemplate.delete(tokenType + ": " + tokenHash);
    }
}
