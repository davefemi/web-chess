package nl.davefemi.webchess.data.repository;

import lombok.RequiredArgsConstructor;
import nl.davefemi.webchess.data.entity.session.TokenEntity;
import nl.davefemi.webchess.exception.UnauthorizedException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Repository
public class TokenStore implements TokenRepository{
    private final RedisTemplate<String, TokenEntity> tokenRedisTemplate;

    @Override
    public void saveToken(String key, TokenEntity token) {
        tokenRedisTemplate.opsForValue().set(key +": " + token.getHash(), token, Duration.between(Instant.now(), token.getExpiresAt()));
    }

    @Override
    public TokenEntity retrieveToken(String key, String tokenHash, boolean deleteOnRetrieval) {
        TokenEntity token = tokenRedisTemplate.opsForValue().get(key + ": " + tokenHash);
        if (token == null)
            throw new UnauthorizedException("Token not found");
        if (deleteOnRetrieval)
            deleteToken(key, tokenHash);
        return token;
    }

    private void deleteToken(String key, String tokenHash){
        tokenRedisTemplate.delete(key + ": " + tokenHash);
    }
}
