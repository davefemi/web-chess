package nl.davefemi.chess.session.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.davefemi.chess.data.entity.session.CredentialEntity;
import nl.davefemi.chess.data.mapper.session.CredentialMapper;
import nl.davefemi.chess.data.repository.CredentialRepository;
import nl.davefemi.chess.exception.InvalidTokenException;
import nl.davefemi.chess.play.model.game.Color;
import nl.davefemi.chess.session.model.Player;
import nl.davefemi.chess.util.TokenGenerator;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
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
    private static final String MESSAGE_ID = "message_id";
    private static final String PLAYER_COLOR = "player_color";

    private final CredentialRepository tokenRepository;
    private final CredentialMapper tokenMapper;

    @Override
    public String getAccessToken(String sessionId) throws InvalidTokenException {
        try {
            return generateCredential(
                    ACCESS_TOKEN,
                    ACCESS_TOKEN_BYTES,
                    ACCESS_TOKEN_TTL,
                    Map.of(SESSION_ID, sessionId));
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

    @Override
    public String getPlayerToken(Player player) throws InvalidTokenException {
        try {
            return generateCredential(
                    PLAYER_TOKEN,
                    PLAYER_TOKEN_BYTES,
                    PLAYER_TOKEN_TTL,
                    Map.of(
                            SESSION_ID, player.getSessionId().toString(),
                            PLAYER_ID, player.getId().toString(),
                            MESSAGE_ID, player.getMessageId(),
                            PLAYER_COLOR, player.getColor().toString()
                    ));
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

    private String generateCredential(String tokenType, int bytes, int timeToLive, Map<String, String> claims)
            throws NoSuchAlgorithmException {
        String token = TokenGenerator.generateToken(bytes);
        CredentialEntity credential = tokenMapper.mapToEntity(hash(token), Instant.now().plusSeconds(timeToLive), claims);
        storeCredential(tokenType, credential);
        return token;
    }

    public UUID authenticateAccessToken(String token) throws InvalidTokenException {
        try{
            CredentialEntity entity = tokenRepository.retrieveCredential(ACCESS_TOKEN, hash(token), true);
            return UUID.fromString(entity.getClaim(SESSION_ID));
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }

    @Override
    public Player authenticatePlayerToken(String token) throws InvalidTokenException {
        try {
            CredentialEntity credential = tokenRepository.retrieveCredential(PLAYER_TOKEN, hash(token), false);
            credential.expiresAtPlusSeconds(PLAYER_TOKEN_TTL);
            storeCredential(PLAYER_TOKEN, credential);
            return new Player(
                    UUID.fromString(credential.getClaim(PLAYER_ID)),
                    credential.getClaim(MESSAGE_ID),
                    UUID.fromString(credential.getClaim(SESSION_ID)),
                    Color.fromString(credential.getClaim(PLAYER_COLOR))
                    );
        } catch (Exception e) {
            throw new InvalidTokenException("Could not authenticate player");
        }
    }

    private void storeCredential(String tokenType, CredentialEntity credential) {
        tokenRepository.saveCredential(tokenType, credential);
        log.info("Executed sessionId={}: credential stored", credential.getClaim(SESSION_ID));
    }

    private String hash(String token) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }
}
