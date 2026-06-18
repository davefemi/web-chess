package nl.davefemi.chess.data.repository;

import nl.davefemi.chess.data.entity.session.CredentialEntity;

public interface CredentialRepository {
    void saveCredential(String tokenType, CredentialEntity credential);
    CredentialEntity retrieveCredential(String tokenType, String tokenHash, boolean deleteOnRetrieval);
}
