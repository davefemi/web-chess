package nl.davefemi.webchess.data.repository;

import nl.davefemi.webchess.data.entity.session.CredentialEntity;

public interface CredentialRepository {
    void saveCredential(String tokenType, CredentialEntity credential);
    CredentialEntity retrieveCredential(String tokenType, String tokenHash, boolean deleteOnRetrieval);
}
