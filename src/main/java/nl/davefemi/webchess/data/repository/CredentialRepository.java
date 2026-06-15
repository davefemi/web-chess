package nl.davefemi.webchess.data.repository;

import nl.davefemi.webchess.data.entity.session.CredentialEntity;

public interface CredentialRepository {
    void saveCredentials(String tokenType, CredentialEntity token);
    CredentialEntity retrieveCredentials(String tokenType, String tokenHash, boolean deleteOnRetrieval);
}
