package nl.davefemi.webchess.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.entity.session.TokenEntity;
import nl.davefemi.webchess.data.mapper.session.TokenMapper;
import nl.davefemi.webchess.data.repository.TokenRepository;
import nl.davefemi.webchess.exception.InvalidPlayerTokenException;
import nl.davefemi.webchess.exception.UnauthorizedException;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.session.Player;
import nl.davefemi.webchess.session.Token;
import org.springframework.stereotype.Component;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public final class GameTokenService implements TokenService{
    private final SecureRandom random = new SecureRandom();
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;

    @Override
    public Token generateAccessToken(String sessionId){
        Token token = generateToken(8, 60);
        token.setClaim("session_id", sessionId);
        try {
            storeToken("access_token", token);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return token;
    }

    public UUID authenticateAccessToken(String token){
            try{
                TokenEntity entity = tokenRepository.retrieveToken("access_token", tokenMapper.mapTokenToTokenHash(token), true);
                return UUID.fromString(entity.getClaims().get("session_id"));
            } catch (UnauthorizedException | NoSuchAlgorithmException e) {
                throw new UnauthorizedException("Access token has expired");
            }
    }

    @Override
    public Token generatePlayerToken(Player player){
        Token token = generateToken(32, 12000);
        token.setClaim("session_id", player.getSessionId().toString());
        token.setClaim("player_id", player.getId().toString());
        token.setClaim("player_color", player.getColor().getColor());
        try {
            storeToken("player_token", token);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return token;
    }

    private Token generateToken(int bytes, int timeToLive){
        byte[] array = new byte[bytes];
        random.nextBytes(array);
        String code = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(array);
        return new Token(code, timeToLive);
    }

    @Override
    public Player authenticatePlayerToken(String token) throws InvalidPlayerTokenException {
        try {
            String tokenHash = tokenMapper.mapTokenToTokenHash(token);
            TokenEntity tokenEntity = tokenRepository.retrieveToken("player_token", tokenHash, false);
            Map<String, String> claims = tokenEntity.getClaims();
            return new Player(UUID.fromString(claims.get("player_id")),
                    UUID.fromString(claims.get("session_id")),
                    Color.fromString(claims.get("player_color"))
                    );
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidPlayerTokenException(e.getMessage());
        }
    }

    private void storeToken(String key, Token token) throws NoSuchAlgorithmException {
        TokenEntity tokenEntity = tokenMapper.mapToEntity(token);
        log.info("Executed sessionId={}: access token stored", token.getClaim("session_id"));
        tokenRepository.saveToken(key, tokenEntity);
    }

}
