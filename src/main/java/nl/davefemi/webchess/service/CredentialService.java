package nl.davefemi.webchess.service;

import nl.davefemi.webchess.exception.InvalidTokenException;
import nl.davefemi.webchess.session.Player;

import java.util.UUID;


public interface CredentialService {

    String getAccessToken(String sessionId) throws InvalidTokenException;
    String getPlayerToken(Player player) throws InvalidTokenException;
    UUID authenticateAccessToken(String token) throws InvalidTokenException;
    Player authenticatePlayerToken(String token) throws InvalidTokenException;
}
