package nl.davefemi.webchess.data.repository;

import nl.davefemi.webchess.data.entity.session.TokenEntity;

public interface TokenRepository {
    void saveToken(String key, TokenEntity token);
    TokenEntity retrieveToken(String key, String tokenHash, boolean deleteOnRetrieval);
}
