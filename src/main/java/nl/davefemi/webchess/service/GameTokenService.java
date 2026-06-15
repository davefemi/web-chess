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
import org.springframework.stereotype.Service;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public final class GameTokenService implements TokenService{
    private static final int ACCESS_TOKEN_BYTES = 8;
    private static final int ACCESS_TOKEN_TTL = 60;
    private static final int PLAYER_TOKEN_BYTES = 32;
    private static final int PLAYER_TOKEN_TTL = 12000;
    private static final String ACCESS_TOKEN = "access_token";
    private static final String PLAYER_TOKEN = "player_token";
    private static final String SESSION_ID = "session_id";
    private static final String PLAYER_ID = "player_id";
    private static final String PLAYER_COLOR = "player_color";
    private final SecureRandom random = new SecureRandom();
    private final TokenRepository tokenRepository;
    private final TokenMapper tokenMapper;

    @Override
    public String generateAccessToken(String sessionId){
        Token token = generateToken(ACCESS_TOKEN_BYTES, ACCESS_TOKEN_TTL);
        token.setClaim(SESSION_ID, sessionId);
        try {
            storeToken(ACCESS_TOKEN, token);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return token.getToken();
    }

    public UUID authenticateAccessToken(String token){
            try{
                TokenEntity entity = tokenRepository.retrieveToken(ACCESS_TOKEN, tokenMapper.mapTokenToTokenHash(token), true);
                return UUID.fromString(entity.getClaims().get(SESSION_ID));
            } catch (UnauthorizedException | NoSuchAlgorithmException e) {
                throw new UnauthorizedException("Access is invalid");
            }
    }

    @Override
    public String generatePlayerToken(Player player){
        Token token = generateToken(PLAYER_TOKEN_BYTES, PLAYER_TOKEN_TTL);
        token.setClaim(SESSION_ID, player.getSessionId().toString());
        token.setClaim(PLAYER_ID, player.getId().toString());
        token.setClaim(PLAYER_COLOR, player.getColor().getColor());
        try {
            storeToken(PLAYER_TOKEN, token);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return token.getToken();
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
            TokenEntity tokenEntity = tokenRepository.retrieveToken(PLAYER_TOKEN, tokenHash, false);
            Map<String, String> claims = tokenEntity.getClaims();
            return new Player(UUID.fromString(claims.get(PLAYER_ID)),
                    UUID.fromString(claims.get(SESSION_ID)),
                    Color.fromString(claims.get(PLAYER_COLOR))
                    );
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidPlayerTokenException(e.getMessage());
        }
    }

    private void storeToken(String key, Token token) throws NoSuchAlgorithmException {
        TokenEntity tokenEntity = tokenMapper.mapToEntity(token);
        log.info("Executed sessionId={}: access token stored", token.getClaim(SESSION_ID));
        tokenRepository.saveToken(key, tokenEntity);
    }

}
