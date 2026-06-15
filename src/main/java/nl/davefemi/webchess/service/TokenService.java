package nl.davefemi.webchess.service;

import nl.davefemi.webchess.exception.InvalidPlayerTokenException;
import nl.davefemi.webchess.session.Player;

import java.util.UUID;


public interface TokenService {

    String generateAccessToken(String sessionId);
    String generatePlayerToken(Player player);
    UUID authenticateAccessToken(String token);
    Player authenticatePlayerToken(String token) throws InvalidPlayerTokenException;
}
