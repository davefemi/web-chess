package nl.davefemi.data.repository;

import nl.davefemi.data.entity.GameSession;
import org.springframework.data.repository.CrudRepository;

public interface GameSessionRepository extends CrudRepository<GameSession, String> {
}
