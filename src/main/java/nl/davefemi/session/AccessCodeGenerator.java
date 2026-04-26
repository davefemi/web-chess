package nl.davefemi.session;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

public final class AccessCodeGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static AccessCode getAccessCode(String sessionId, int timeToLive){
        byte[] bytes = new byte[8];
        random.nextBytes(bytes);
        String code = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
        return new AccessCode(code, sessionId, Instant.now().plusSeconds(timeToLive));
    }
}
