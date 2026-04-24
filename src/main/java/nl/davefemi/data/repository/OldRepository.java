package nl.davefemi.data.repository;

import nl.davefemi.data.entity.GameSessionEntity;
import org.springframework.data.repository.CrudRepository;

public interface OldRepository extends CrudRepository<GameSessionEntity, String> {
}
