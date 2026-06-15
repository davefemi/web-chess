package nl.davefemi.webchess.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.webchess.data.entity.session.CredentialEntity;
import nl.davefemi.webchess.data.mapper.session.CredentialMapper;
import nl.davefemi.webchess.data.repository.CredentialRepository;
import nl.davefemi.webchess.exception.InvalidTokenException;
import nl.davefemi.webchess.game.Color;
import nl.davefemi.webchess.session.Player;
import nl.davefemi.webchess.session.Credential;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public final class GameCredentialService implements CredentialService {
    private static final int ACCESS_TOKEN_BYTES = 8;
    private static final int ACCESS_TOKEN_TTL = 60;
    private static final int PLAYER_TOKEN_BYTES = 32;
    private static final int PLAYER_TOKEN_TTL = 300;
    private static final String ACCESS_TOKEN = "access_token";
    private static final String PLAYER_TOKEN = "player_token";
    private static final String SESSION_ID = "session_id";
    private static final String PLAYER_ID = "player_id";
    private static final String PLAYER_COLOR = "player_color";
    private final SecureRandom random = new SecureRandom();
    private final CredentialRepository tokenRepository;
    private final CredentialMapper tokenMapper;

    @Override
    public String generateAccessToken(String sessionId){
        try {
            Credential credential = generateToken(ACCESS_TOKEN_BYTES, ACCESS_TOKEN_TTL);
            credential.setClaim(SESSION_ID, sessionId);
            storeCredential(ACCESS_TOKEN, tokenMapper.mapToEntity(credential));
            return credential.getToken();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public UUID authenticateAccessToken(String token) throws InvalidTokenException {
            try{
                CredentialEntity entity = tokenRepository.retrieveCredentials(ACCESS_TOKEN, hash(token), true);
                return UUID.fromString(entity.getClaims().get(SESSION_ID));
            } catch (Exception e) {
                throw new InvalidTokenException("Access token is invalid");
            }
    }

    @Override
    public String generatePlayerToken(Player player){
        try {
            Credential credential = generateToken(PLAYER_TOKEN_BYTES, PLAYER_TOKEN_TTL);
            credential.setClaim(SESSION_ID, player.getSessionId().toString());
            credential.setClaim(PLAYER_ID, player.getId().toString());
            credential.setClaim(PLAYER_COLOR, player.getColor().getColor());
            storeCredential(PLAYER_TOKEN, tokenMapper.mapToEntity(credential));
            return credential.getToken();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Credential generateToken(int bytes, int timeToLive) throws NoSuchAlgorithmException {
        byte[] array = new byte[bytes];
        random.nextBytes(array);
        String code = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(array);
        return new Credential(code, timeToLive);
    }

    @Override
    public Player authenticatePlayerToken(String token) throws InvalidTokenException {
        try {
            String tokenHash = hash(token);
            CredentialEntity credential = tokenRepository.retrieveCredentials(PLAYER_TOKEN, tokenHash, false);
            Map<String, String> claims = credential.getClaims();
            credential.setExpiresAt(Instant.now().plusSeconds(PLAYER_TOKEN_TTL));
            storeCredential(PLAYER_TOKEN, credential);
            return new Player(UUID.fromString(claims.get(PLAYER_ID)),
                    UUID.fromString(claims.get(SESSION_ID)),
                    Color.fromString(claims.get(PLAYER_COLOR))
                    );
        } catch (Exception e) {
            throw new InvalidTokenException("Could not authenticate player");
        }
    }

    private void storeCredential(String key, CredentialEntity credential) throws NoSuchAlgorithmException {
        log.info("Executed sessionId={}: access token stored", credential.getClaims().get(SESSION_ID));
        tokenRepository.saveCredentials(key, credential);
    }

    private String hash(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }

}
