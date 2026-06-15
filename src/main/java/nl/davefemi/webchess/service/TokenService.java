package nl.davefemi.webchess.service;

import nl.davefemi.webchess.exception.InvalidPlayerTokenException;
import nl.davefemi.webchess.session.Player;
import nl.davefemi.webchess.session.Token;

import java.util.UUID;


public interface TokenService {

    Token generateAccessToken(String sessionId);
    Token generatePlayerToken(Player player);
    UUID authenticateAccessToken(String token);
    Player authenticatePlayerToken(String token) throws InvalidPlayerTokenException;
}
