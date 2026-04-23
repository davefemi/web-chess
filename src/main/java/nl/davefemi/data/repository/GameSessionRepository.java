package nl.davefemi.data.repository;

import nl.davefemi.data.entity.GameSessionEntity;
import org.springframework.data.repository.CrudRepository;

public interface GameSessionRepository extends CrudRepository<GameSessionEntity, String> {
}
