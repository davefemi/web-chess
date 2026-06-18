package nl.davefemi.chess.util;

import java.security.SecureRandom;
import java.util.Base64;

public final class TokenGenerator {
    private static final SecureRandom random = new SecureRandom();

    public static String generateToken(int bytes){
        byte[] array = new byte[bytes];
        random.nextBytes(array);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(array);
    }
}
